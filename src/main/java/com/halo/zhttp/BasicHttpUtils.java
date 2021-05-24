package com.halo.zhttp;

import com.halo.zhttp.assembler.AssemblerException;
import com.halo.zhttp.assembler.AssemblyLine;
import com.halo.zhttp.utils.CommonUtils;
import com.halo.zhttp.utils.UtilsException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class BasicHttpUtils<R> implements HttpUtils {

    private AssemblyLine assemblyLine;

    private Class<R> rClass;

    protected String url;

    protected Map<String, String> args;

    private R processResponse(String urlWithArgs, CloseableHttpClient httpClient, HttpRequestBase httpRequest) throws HttpUtilsException, IOException {
        if (rClass == File.class) {
            ResponseHandler<File> responseHandler = CommonUtils.getResponseHandler(File.class);

            File downloadFile;
            try {
                downloadFile = httpClient.execute(httpRequest, responseHandler);
            } catch (IOException e) {
                throw new HttpUtilsException("Download from " + urlWithArgs + " error.", e);
            } finally {
                httpClient.close();
            }

            return (R) downloadFile;
        } else {
            ResponseHandler<String> responseHandler = CommonUtils.getResponseHandler(String.class);

            String responseBody;
            try {
                responseBody = httpClient.execute(httpRequest, responseHandler);
            } catch (IOException e) {
                throw new HttpUtilsException("Get from " + urlWithArgs + " error.", e);
            } finally {
                httpClient.close();
            }

            return (R) responseBody;
        }
    }

    public BasicHttpUtils(Class<R> rClass, String url) {
        this.rClass = rClass;
        this.url = url;
    }

    public void setAssemblyLine(AssemblyLine assemblyLine) {
        this.assemblyLine = assemblyLine;
    }

    public AssemblyLine getAssemblyLine() {
        return assemblyLine;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    @Override
    public R get() throws HttpUtilsException, IOException {
        // create url with args (if has)
        String urlWithArgs = null;
        try {
            urlWithArgs = CommonUtils.getUrlWithArgs(url, args);
        } catch (UtilsException e) {
            throw new HttpUtilsException("Build url with args error.", e);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlWithArgs);

        if (null != assemblyLine) {
            try {
                assemblyLine.execute(httpGet);
            } catch (AssemblerException e) {
                throw new HttpUtilsException("Assemble http request error.", e);
            }
        }

        // ResponseHandler<String> responseHandler = CommonUtils.getStringResponseHandler();

        return processResponse(urlWithArgs, httpClient, httpGet);
    }

    @Override
    public R post() throws HttpUtilsException, IOException {
        // create url with args (if has)
        String urlWithArgs = null;
        try {
            urlWithArgs = CommonUtils.getUrlWithArgs(url, args);
        } catch (UtilsException e) {
            throw new HttpUtilsException("Build url with args error.", e);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlWithArgs);

        if (null != assemblyLine) {
            try {
                assemblyLine.execute(httpPost);
            } catch (AssemblerException e) {
                throw new HttpUtilsException("Assemble http request error.", e);
            }
        }

        // ResponseHandler<String> responseHandler = CommonUtils.getStringResponseHandler();

        return processResponse(urlWithArgs, httpClient, httpPost);
    }

}
