package com.halo.zhttp.assembler;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

public class RequestBodyAssembler extends HttpRequestAssembler {

    private String requestBody;

    private String contentType;

    public RequestBodyAssembler(String requestBody, String contentType) {
        this.requestBody = requestBody;
        this.contentType = contentType;
    }

    @Override
    public void assemble(HttpRequestBase httpRequest) throws AssemblerException {
        if (null != requestBody && !requestBody.isEmpty()) {
            StringEntity reqEntity = new StringEntity(requestBody, "UTF-8");
            reqEntity.setContentEncoding("UTF-8");
            if (null != contentType && !contentType.isEmpty()) {
                reqEntity.setContentType(contentType);
            } else {
                throw new AssemblerException("Content type can not be null.");
            }
            ((HttpPost) httpRequest).setEntity(reqEntity);
        } else {
            throw new AssemblerException("Request body can not be null.");
        }
    }
}
