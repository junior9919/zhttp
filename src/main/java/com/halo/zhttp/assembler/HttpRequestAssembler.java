package com.halo.zhttp.assembler;

import org.apache.http.client.methods.HttpRequestBase;

public abstract class HttpRequestAssembler {

    public abstract void assemble(HttpRequestBase httpRequest) throws AssemblerException;
}
