    public static String haalPaginaOp(String domein, String target) throws Exception {
        String ans = "";
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
        BasicHttpContext context = new BasicHttpContext(null);
        HttpHost host = new HttpHost(domein, 80);
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();
        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
        try {
            if (!conn.isOpen()) {
                Socket socket = new Socket(host.getHostName(), host.getPort());
                conn.bind(socket, params);
            }
            BasicHttpRequest request = new BasicHttpRequest("GET", target);
            System.out.println(">> Request URI: " + request.getRequestLine().getUri());
            request.setParams(params);
            httpexecutor.preProcess(request, httpproc, context);
            HttpResponse response = httpexecutor.execute(request, conn, context);
            response.setParams(params);
            httpexecutor.postProcess(response, httpproc, context);
            System.out.println("<< Response: " + response.getStatusLine());
            String foobar = (EntityUtils.toString(response.getEntity()));
            ans = foobar;
            System.out.println(foobar);
            System.out.println("ans:\n" + ans);
            System.out.println("==============");
            if (!connStrategy.keepAlive(response, context)) {
                conn.close();
            } else {
            }
        } finally {
            conn.close();
        }
        return ans;
    }
