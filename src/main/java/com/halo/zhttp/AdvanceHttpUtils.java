package com.halo.zhttp;

import com.halo.zhttp.assembler.AssemblyLine;
import com.halo.zhttp.assembler.RequestBodyAssembler;
import com.halo.zjson.JSONUtils;

import java.io.IOException;
import java.util.Map;

public class AdvanceHttpUtils<P, R> extends BasicHttpUtils implements HttpUtils {

    private final String JSON_CONTENT_TYPE = "application/json";

    private JSONUtils<P> requestJsonUtils;

    private P requestJsonBean;

    private JSONUtils<R> responseJsonUtils;

    private Map<String, Class> classMap;

    public AdvanceHttpUtils(String url) {
        super(String.class, url);
    }

    public void setRequestJsonUtils(JSONUtils<P> requestJsonUtils) {
        this.requestJsonUtils = requestJsonUtils;
    }

    public void setRequestJsonBean(P requestJsonBean) {
        this.requestJsonBean = requestJsonBean;
    }

    public void setResponseJsonUtils(JSONUtils<R> responseJsonUtils) {
        this.responseJsonUtils = responseJsonUtils;
    }

    public void setClassMap(Map<String, Class> classMap) {
        this.classMap = classMap;
    }

    @Override
    public R get() throws HttpUtilsException, IOException {
        String responseBody = (String) super.get();
        if (null != responseJsonUtils) {
            if (null != classMap) {
                return responseJsonUtils.getComplexJsonBean(responseBody, classMap);
            } else {
                return responseJsonUtils.getJsonBean(responseBody);
            }
        } else {
            throw new HttpUtilsException("Response JSONUtils unassigned.");
        }
    }

    @Override
    public R post() throws HttpUtilsException, IOException {
        if (null != requestJsonBean) {
            String requestJsonStr = null;
            if (null != requestJsonUtils) {
                requestJsonStr = requestJsonUtils.getJsonStr(requestJsonBean);
            } else {
                throw new HttpUtilsException("Request JSONUtils unassigned.");
            }

            AssemblyLine assemblyLine = getAssemblyLine();
            if (null == assemblyLine) {
                assemblyLine = new AssemblyLine();
                setAssemblyLine(assemblyLine);
            }
            RequestBodyAssembler requestBodyAssembler = new RequestBodyAssembler(requestJsonStr, JSON_CONTENT_TYPE);
            assemblyLine.addAssembler(requestBodyAssembler);
        }

        String responseBody = (String) super.post();
        if (null != responseJsonUtils) {
            if (null != classMap) {
                return responseJsonUtils.getComplexJsonBean(responseBody, classMap);
            } else {
                return responseJsonUtils.getJsonBean(responseBody);
            }
        } else {
            throw new HttpUtilsException("Response JSONUtils unassigned.");
        }
    }
}
