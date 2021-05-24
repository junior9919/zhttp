zhttp是一个封装了httpcomponents的工具，用于简化httpcomponents的操作。

用httpcomponents发送一个get请求，至少需要这样：

	CloseableHttpClient httpClient = HttpClients.createDefault();
	HttpGet httpGet = new HttpGet(url);

	// Create a custom response handler
	ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

		private HttpResponse response;

		@Override
		public String handleResponse(final HttpResponse response) throws IOException {
			this.response = response;
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		}

	};
	String responseBody;
	try {
		responseBody = httpClient.execute(httpGet, responseHandler);
	} catch (IOException e) {
		throw new HttpUtilsException("Get from " + url + " error.", e);
	} finally {
		httpClient.close();
	}
	
用zhttp封装后，现在发送一个get请求，只需要以下几步：

	BasicHttpUtils basicHttpUtils = new BasicHttpUtils(String.class, url);

	String responseBody;
	try {
		responseBody = (String) basicHttpUtils.get();
	} catch (HttpUtilsException e) {
		throw new ServiceException("Get from " + url + " error.", e);
	} catch (IOException e) {
		throw new ServiceException("Get from " + url + " error.", e);
	}
	
zhttp还提供了发送post请求、下载文件、发送带Authorization Header的HTTP请求、在请求体中带上表单数据、在请求体中带上文本或json数据等功能。

下面我们介绍zhttp的使用。

一、在项目中引用zhttp；

1.1 下载“zjson-1.7.jar”到本地（下载地址：https://github.com/junior9919/zjson/releases/download/1.7/zjson-1.7.jar）；
	
1.2 用命令“mvn install:install-file -Dfile=D:\idea-workspace\zjson\target\zjson-1.7.jar -DgroupId=com.halo -DartifactId=zjson -Dversion=1.7 -Dpackaging=jar”将zjson安装到本地maven仓库；
	
1.3 在pom.xml文件中加入引用（在build-pluginManagement-plugins小节，如果已有maven-install-plugin就不需要再加了）：
	
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <version>2.5.2</version>
    </plugin>
				
1.4 在pom.xml文件中加入以下依赖（在dependencies小节）：
	
    <!-- json-lib and utils dependencies -->
    <dependency>
        <groupId>net.sf.json-lib</groupId>
        <artifactId>json-lib</artifactId>
        <version>2.4</version>
        <classifier>jdk15</classifier>
    </dependency>
    
    <dependency>
        <groupId>com.halo</groupId>
        <artifactId>zjson</artifactId>
        <version>1.7</version>
    </dependency>
    <!-- json-lib and utils dependencies -->
		
1.5 下载zhttp-2.3.jar到本地（下载地址：https://github.com/junior9919/zjson/releases/download/2.3/zhttp-2.3.jar）；
	
1.6 用命令“mvn install:install-file -Dfile=D:\idea-workspace\zhttp\target\zhttp-2.3.jar -DgroupId=com.halo -DartifactId=zhttp -Dversion=2.3 -Dpackaging=jar”将zhttp安装到本地maven仓库；
	
1.7 在pom.xml文件中加入以下依赖（在dependencies小节）：
	
    <!-- http client and utils dependencies -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpcore</artifactId>
        <version>4.4.10</version>
    </dependency>
    
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.6</version>
    </dependency>
    
    <dependency>
        <groupId>com.halo</groupId>
        <artifactId>zhttp</artifactId>
        <version>2.3</version>
    </dependency>
    <!-- http client and utils dependencies -->
		
二、zhttp的使用；

2.1 最简单的GET请求上面已经介绍过，现在来看看URL后带参数的get请求：
	
    BasicHttpUtils basicHttpUtils = new BasicHttpUtils(String.class, url);
    
    Map<String, String> args = new HashMap<>();
    args.put("account", account);
    args.put("accessToken", accessToken);
    args.put("client", "h5");
    
    basicHttpUtils.setArgs(args);
    
    String responseBody;
    try {
        responseBody = (String) basicHttpUtils.get();
    } catch (HttpUtilsException e) {
        throw new ServiceException("Get from " + url + " error.", e);
    } catch (IOException e) {
        throw new ServiceException("Get from " + url + " error.", e);
    }
		
2.2 POST请求，将上面的“get”换成“post”即可：
	
    BasicHttpUtils basicHttpUtils = new BasicHttpUtils(String.class, url);
    
    Map<String, String> args = new HashMap<>();
    args.put("account", account);
    args.put("accessToken", accessToken);
    args.put("client", "h5");
    
    basicHttpUtils.setArgs(args);
    
    String responseBody;
    try {
        responseBody = (String) basicHttpUtils.post();
    } catch (HttpUtilsException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    } catch (IOException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    }
		
2.3 HTTP表单里带数据的POST请求：
	
    BasicHttpUtils basicHttpUtils = new BasicHttpUtils(String.class, url);
    
    AssemblyLine assemblyLine = basicHttpUtils.getAssemblyLine();
    if (null == assemblyLine) {
        assemblyLine = new AssemblyLine();
        basicHttpUtils.setAssemblyLine(assemblyLine);
    }
    
    List<BasicNameValuePair> formData = new ArrayList<>();
    formData.add(new BasicNameValuePair("grant_type", "authorization_code"));
    formData.add(new BasicNameValuePair("code", code));
    formData.add(new BasicNameValuePair("redirect_uri", redirectUrl));
    
    FormDataAssembler formDataAssembler = new FormDataAssembler(formData);
    assemblyLine.addAssembler(formDataAssembler);
    
    String responseBody;
    try {
        responseBody = (String) basicHttpUtils.post();
    } catch (HttpUtilsException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    } catch (IOException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    }
		
2.4 在请求体中带上文本或json数据的POST请求：
	
    BasicHttpUtils basicHttpUtils = new BasicHttpUtils(String.class, url);
    
    AssemblyLine assemblyLine = basicHttpUtils.getAssemblyLine();
    if (null == assemblyLine) {
        assemblyLine = new AssemblyLine();
        basicHttpUtils.setAssemblyLine(assemblyLine);
    }
    
    RequestBodyAssembler requestBodyAssembler = new RequestBodyAssembler("{ "id": 1 }", "application/json");
    assemblyLine.addAssembler(formDataAssembler);
    
    String responseBody;
    try {
        responseBody = (String) basicHttpUtils.post();
    } catch (HttpUtilsException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    } catch (IOException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    }
		
2.5 带Authorization Header的HTTP请求，目前只实现了Basic验证：
	
    BasicHttpUtils basicHttpUtils = new BasicHttpUtils(String.class, url);
    
    AssemblyLine assemblyLine = basicHttpUtils.getAssemblyLine();
    if (null == assemblyLine) {
        assemblyLine = new AssemblyLine();
        basicHttpUtils.setAssemblyLine(assemblyLine);
    }
    
    String clientId = "25841d1961f25b427fc8";
    String clientSecret = "fd57902b2fb7243f524814ab640361";
    AuthorizationHeaderAssembler authorizationHeaderAssembler =
            new AuthorizationHeaderAssembler(clientId, clientSecret, AuthorizationType.Basic);
    assemblyLine.addAssembler(authorizationHeaderAssembler);
    
    String responseBody;
    try {
        responseBody = (String) basicHttpUtils.post();
    } catch (HttpUtilsException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    } catch (IOException e) {
        throw new ServiceException("Post from " + url + " error.", e);
    }
		
2.6 使用“AdvanceHttpUtils”，可以将Java对象（POJO对象）转化成json，并将返回结果的json转化成Java对象：
	
    AdvanceHttpUtils advanceHttpUtils = new AdvanceHttpUtils(url);
    
    advanceHttpUtils.setRequestJsonUtils(new JSONUtils(AccessTokenRequestBean.class));
    
    AccessTokenRequestBean accessTokenRequestBean = new AccessTokenRequestBean();
    accessTokenRequestBean.setUserId(userId);
    accessTokenRequestBean.setPassword(password);
    advanceHttpUtils.setRequestJsonBean(accessTokenRequestBean);
    
    advanceHttpUtils.setResponseJsonUtils(new JSONUtils(AccessTokenResponseBean.class));
    
    AccessTokenResponseBean accessTokenResponseBean;
    try {
        accessTokenResponseBean = (AccessTokenResponseBean) advanceHttpUtils.post();
    } catch (HttpUtilsException e) {
        throw new ServiceException("Get login access token error.", e);
    } catch (IOException e) {
        throw new ServiceException("Get login access token error.", e);
    }
		
2.7 下载文件：
	
    BasicHttpUtils basicHttpUtils = new BasicHttpUtils<>(File.class, url);
    File downloadFile = null;
    try {
        downloadFile = (File) basicHttpUtils.get();
    } catch (HttpUtilsException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    System.out.println(downloadFile.getAbsolutePath());
