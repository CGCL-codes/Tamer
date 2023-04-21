    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: <target URI> <no of requests>");
            System.exit(-1);
        }
        URI targetURI = new URI(args[0]);
        int n = Integer.parseInt(args[1]);
        HttpHost targetHost = new HttpHost(targetURI.getHost(), targetURI.getPort());
        BasicHttpParams params = new BasicHttpParams();
        params.setParameter(HttpProtocolParams.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        params.setBooleanParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        params.setBooleanParameter(HttpConnectionParams.STALE_CONNECTION_CHECK, false);
        params.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 8 * 1024);
        BasicHttpRequest httpget = new BasicHttpRequest("GET", targetURI.getPath());
        byte[] buffer = new byte[4096];
        long startTime;
        long finishTime;
        int successCount = 0;
        int failureCount = 0;
        String serverName = "unknown";
        long total = 0;
        long contentLen = 0;
        long totalContentLen = 0;
        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new RequestContent());
        httpproc.addInterceptor(new RequestTargetHost());
        httpproc.addInterceptor(new RequestConnControl());
        httpproc.addInterceptor(new RequestUserAgent());
        httpproc.addInterceptor(new RequestExpectContinue());
        HttpContext context = new BasicHttpContext();
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        DefaultConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            if (!conn.isOpen()) {
                Socket socket = new Socket(targetHost.getHostName(), targetHost.getPort() > 0 ? targetHost.getPort() : 80);
                conn.bind(socket, params);
            }
            context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, targetHost);
            httpexecutor.preProcess(httpget, httpproc, context);
            HttpResponse response = httpexecutor.execute(httpget, conn, context);
            httpexecutor.postProcess(response, httpproc, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    contentLen = 0;
                    if (instream != null) {
                        int l = 0;
                        while ((l = instream.read(buffer)) != -1) {
                            total += l;
                            contentLen += l;
                        }
                    }
                    successCount++;
                    totalContentLen += contentLen;
                } catch (IOException ex) {
                    conn.shutdown();
                    failureCount++;
                } finally {
                    instream.close();
                }
            }
            if (!connStrategy.keepAlive(response, context)) {
                conn.close();
            }
            Header header = response.getFirstHeader("Server");
            if (header != null) {
                serverName = header.getValue();
            }
        }
        finishTime = System.currentTimeMillis();
        float totalTimeSec = (float) (finishTime - startTime) / 1000;
        float reqsPerSec = (float) successCount / totalTimeSec;
        float timePerReqMs = (float) (finishTime - startTime) / (float) successCount;
        System.out.print("Server Software:\t");
        System.out.println(serverName);
        System.out.println();
        System.out.print("Document URI:\t\t");
        System.out.println(targetURI);
        System.out.print("Document Length:\t");
        System.out.print(contentLen);
        System.out.println(" bytes");
        System.out.println();
        System.out.print("Time taken for tests:\t");
        System.out.print(totalTimeSec);
        System.out.println(" seconds");
        System.out.print("Complete requests:\t");
        System.out.println(successCount);
        System.out.print("Failed requests:\t");
        System.out.println(failureCount);
        System.out.print("Content transferred:\t");
        System.out.print(total);
        System.out.println(" bytes");
        System.out.print("Requests per second:\t");
        System.out.print(reqsPerSec);
        System.out.println(" [#/sec] (mean)");
        System.out.print("Time per request:\t");
        System.out.print(timePerReqMs);
        System.out.println(" [ms] (mean)");
    }
