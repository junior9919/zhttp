package com.halo.zhttp.assembler;

import org.apache.http.client.methods.HttpRequestBase;

import java.util.ArrayList;
import java.util.List;

public class AssemblyLine {

    private List<HttpRequestAssembler> assemblers = new ArrayList<>();

    public void addAssembler(HttpRequestAssembler assembler) {
        this.assemblers.add(assembler);
    }

    public void execute(HttpRequestBase httpRequest) throws AssemblerException {
        for (HttpRequestAssembler assembler : assemblers) {
            assembler.assemble(httpRequest);
        }
    }
}
