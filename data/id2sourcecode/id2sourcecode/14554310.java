    public static void main(String[] args) throws Exception {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
        HttpProtocolParams.setUseExpectContinue(params, true);
        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new RequestContent());
        httpproc.addInterceptor(new RequestTargetHost());
        httpproc.addInterceptor(new RequestConnControl());
        httpproc.addInterceptor(new RequestUserAgent());
        httpproc.addInterceptor(new RequestExpectContinue());
        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
        HttpContext context = new BasicHttpContext(null);
        HttpHost host = new HttpHost("localhost", 8080);
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();
        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
        try {
            HttpEntity[] requestBodies = { new StringEntity("This is the first test request", "UTF-8"), new ByteArrayEntity("This is the second test request".getBytes("UTF-8")), new InputStreamEntity(new ByteArrayInputStream("This is the third test request (will be chunked)".getBytes("UTF-8")), -1) };
            for (int i = 0; i < requestBodies.length; i++) {
                if (!conn.isOpen()) {
                    Socket socket = new Socket(host.getHostName(), host.getPort());
                    conn.bind(socket, params);
                }
                BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", "/servlets-examples/servlet/RequestInfoExample");
                request.setEntity(requestBodies[i]);
                System.out.println(">> Request URI: " + request.getRequestLine().getUri());
                request.setParams(params);
                httpexecutor.preProcess(request, httpproc, context);
                HttpResponse response = httpexecutor.execute(request, conn, context);
                response.setParams(params);
                httpexecutor.postProcess(response, httpproc, context);
                System.out.println("<< Response: " + response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println("==============");
                if (!connStrategy.keepAlive(response, context)) {
                    conn.close();
                } else {
                    System.out.println("Connection kept alive...");
                }
            }
        } finally {
            conn.close();
        }
    }
