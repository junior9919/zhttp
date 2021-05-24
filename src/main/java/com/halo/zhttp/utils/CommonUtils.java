package com.halo.zhttp.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtils {

    private static File saveStreamToFile(InputStream stream) throws UtilsException, IOException {
        UUID uuid = new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong());
        File downloadFile = new File(uuid.toString() + ".dld");
        try {
            boolean newFile = downloadFile.createNewFile();
        } catch (IOException e) {
            throw new UtilsException("Can't create new download file. ", e);
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(downloadFile);

            int size;
            byte[] bytes = new byte[1024];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
            while ((size = bufferedInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, size);
            }
        } catch (FileNotFoundException e) {
            throw new UtilsException("Can't create a FileOutputStream.", e);
        } catch (IOException e) {
            throw new UtilsException("Can't read from input stream or write to output stream.", e);
        } finally {
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }

        return downloadFile;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    public static String encryptKey(String id, String key, String timeStamp) throws UtilsException {
        String originHash = id + key + timeStamp;

        byte[] tmpKey;
        try {
            tmpKey = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UtilsException("Unsupported encoding type UTF-8.", e);
        }
        SecretKey secretKey = new SecretKeySpec(tmpKey, "HmacSHA1");
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new UtilsException("There is no such algorithm HmacSHA1. ", e);
        }
        try {
            mac.init(secretKey);
        } catch (InvalidKeyException e) {
            throw new UtilsException("Invalid secret key. ", e);
        }
        byte[] text;
        try {
            text = originHash.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UtilsException("Unsupported encoding type UTF-8.", e);
        }
        byte[] encryptText = mac.doFinal(text);
        String encryptHash = byte2hex(encryptText);

        return encryptHash;
    }

    public static String getUrlWithArgs(String url, Map<String, String> args) throws UtilsException {
        StringBuilder params = new StringBuilder();
        if (null != args && args.size() > 0) {
            for (String paramName : args.keySet()) {
                String paramValue;
                try {
                    if (null == args.get(paramName)) {
                        paramValue = "";
                    } else {
                        paramValue = URLEncoder.encode(args.get(paramName), "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new UtilsException("URL encode error.", e);
                }
                params.append(paramName).append("=").append(paramValue).append("&");
            }
        }
        if (params.length() > 0) {
            params = new StringBuilder(params.substring(0, params.length() - 1));
        }

        String urlWithArgs = url;
        if (params.length() > 0) {
            urlWithArgs += "?" + params;
        }
        return urlWithArgs;
    }

    public static String getAuthorizationString(String user, String password) throws UtilsException {
        String tempAuthString = user + ":" + password;
        byte[] tempBytes = new byte[0];
        try {
            tempBytes = tempAuthString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UtilsException("Unsupported encoding UTF-8.", e);
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(tempBytes);
    }

    public static <T> ResponseHandler<T> getResponseHandler(Class<T> tClass) {
        // Create a custom response handler
        return new ResponseHandler<T>() {

            private HttpResponse response;

            @Override
            public T handleResponse(final HttpResponse response) throws IOException {
                this.response = response;
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    if (tClass == File.class) {
                        if (entity == null) {
                            throw new ClientProtocolException("There's no content in http response.");
                        }
                        try {
                            return (T) saveStreamToFile(entity.getContent());
                        } catch (UtilsException e) {
                            throw new IOException("Save file error.", e);
                        }
                    } else {
                        return entity != null ? (T) EntityUtils.toString(entity, "UTF-8") : null;
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
    }
}
