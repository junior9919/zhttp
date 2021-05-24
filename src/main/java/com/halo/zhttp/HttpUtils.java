package com.halo.zhttp;

import java.io.IOException;

public interface HttpUtils<R> {

    R get() throws HttpUtilsException, IOException;

    R post() throws HttpUtilsException, IOException;
}
