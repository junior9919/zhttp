package com.halo.zhttp.assembler;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class FormDataAssembler extends HttpRequestAssembler {

    private List<BasicNameValuePair> formData;

    public FormDataAssembler(List<BasicNameValuePair> formData) {
        this.formData = formData;
    }

    @Override
    public void assemble(HttpRequestBase httpRequest) throws AssemblerException {
        if (null != formData && formData.size() > 0) {
            try {
                ((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(formData));
            } catch (UnsupportedEncodingException e) {
                throw new AssemblerException("Set request entity error.", e);
            }
        } else {
            throw new AssemblerException("Form data can not be null.");
        }
    }
}
