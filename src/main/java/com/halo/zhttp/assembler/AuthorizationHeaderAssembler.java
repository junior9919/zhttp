package com.halo.zhttp.assembler;

import com.halo.zhttp.utils.CommonUtils;
import com.halo.zhttp.utils.UtilsException;
import org.apache.http.client.methods.HttpRequestBase;

public class AuthorizationHeaderAssembler extends HttpRequestAssembler {

    private String user;

    private String password;

    private AuthorizationType authorizationType;

    public AuthorizationHeaderAssembler(String user, String password, AuthorizationType authorizationType) {
        this.user = user;
        this.password = password;
        this.authorizationType = authorizationType;
    }

    @Override
    public void assemble(HttpRequestBase httpRequest) throws AssemblerException {
        if (null != user && null != password && null != authorizationType) {
            switch (authorizationType) {
                case Basic:
                    String authorizationString = null;
                    try {
                        authorizationString = CommonUtils.getAuthorizationString(user, password);
                    } catch (UtilsException e) {
                        throw new AssemblerException("Assemble authorization header error. ", e);
                    }
                    httpRequest.addHeader("Authorization", "Basic " + authorizationString);
                    break;
            }
        }
    }
}
