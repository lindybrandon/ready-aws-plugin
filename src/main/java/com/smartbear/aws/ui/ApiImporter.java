package com.smartbear.aws.ui;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestMethod;
import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.rest.support.RestParamProperty;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.support.UISupport;
import com.eviware.x.dialogs.Worker;
import com.eviware.x.dialogs.XProgressDialog;
import com.eviware.x.dialogs.XProgressMonitor;
import com.smartbear.aws.ApplicationException;
import com.smartbear.aws.Strings;
import com.smartbear.aws.amazon.ApiReader;
import com.smartbear.aws.entity.Api;
import com.smartbear.aws.entity.ApiDescription;
import com.smartbear.aws.entity.HttpMethod;
import com.smartbear.aws.entity.HttpResource;
import com.smartbear.aws.entity.MethodParameter;

import java.util.ArrayList;
import java.util.List;

public class ApiImporter implements Worker {
    private Boolean canceled = false;
    private final XProgressDialog waitDialog;
    private final ApiReader reader;
    private final List<ApiDescription> apis;
    private final WsdlProject project;
    private final List<RestService> addedServices = new ArrayList<>();
    private final StringBuilder errors = new StringBuilder();

    public ApiImporter(XProgressDialog waitDialog, ApiReader reader, List<ApiDescription> apis, WsdlProject project) {
        this.waitDialog = waitDialog;
        this.reader = reader;
        this.apis = apis;
        this.project = project;
    }

    public static List<RestService> importServices(ApiReader reader, List<ApiDescription> apis, WsdlProject project) {
        XProgressDialog dlg = UISupport.getDialogs().createProgressDialog(Strings.ApiImporter.IMPORT_PROGRESS, 100, "", true);
        ApiImporter worker = new ApiImporter(dlg, reader, apis, project);
        try {
            worker.waitDialog.run(worker);
        } catch (Exception ex) {
            UISupport.showErrorMessage(ex.getMessage());
            SoapUI.logError(ex);
        }
        return worker.addedServices;
    }


    @Override
    public Object construct(XProgressMonitor xProgressMonitor) {
        for (ApiDescription description: apis) {
            try {
                Api api = reader.getApi(description);
                RestService service = build(api, project, reader.getRegion());
                addedServices.add(service);
            } catch (ApplicationException ex) {
                SoapUI.logError(ex);
                errors.append(String.format(Strings.ApiImporter.IMPORT_ERROR, description.name, ex.getMessage()));
            }
        }

        if (errors.length() > 0) {
            errors.append(Strings.ApiImporter.IMPORT_ERROR_TAIL);
        }

        return null;
    }

    @Override
    public void finished() {
        if (canceled) {
            return;
        }
        waitDialog.setVisible(false);
        if (errors.length() > 0) {
            UISupport.showErrorMessage(errors.toString());
        }
    }

    @Override
    public boolean onCancel() {
        canceled = true;
        waitDialog.setVisible(false);
        return true;
    }

    private RestService build(Api api, WsdlProject wsdlProject, String region) {
        try {
            RestService restService = (RestService)wsdlProject.addNewInterface(api.name, RestServiceFactory.REST_TYPE);
            restService.setDescription(api.description);
            restService.setBasePath(api.baseUrl);
            String endPoint = String.format("https://%s.execute-api.%s.amazonaws.com", api.id, region);
            restService.addEndpoint(endPoint);

            RestResource root = api.stage == null ?
                    restService.addNewResource("root", ""):
                    restService.addNewResource(api.stage.name, api.stage.name);
            root.setDescription(api.stage == null ? "" : api.stage.description);
            addResources(root, api.rootResource);
            return restService;
        } catch (Exception e) {
            return null;
        }
    }

    private void addResources(RestResource target, HttpResource source) {
        for (HttpResource child: source.resources) {
            RestResource restResource = target.addNewChildResource(child.name, child.path);
            for (HttpMethod method: child.methods) {
                RestMethod restMethod = restResource.addNewMethod(method.name);
                restMethod.setMethod(method.httpMethod);
                for (MethodParameter param: method.parameters) {
                    RestParamProperty prop = restMethod.addProperty(param.name);
                    prop.setStyle(param.style);
                    prop.setRequired(param.isRequired);
                }
                restMethod.addNewRequest("Request 1");
            }
            addResources(restResource, child);
        }
    }
}