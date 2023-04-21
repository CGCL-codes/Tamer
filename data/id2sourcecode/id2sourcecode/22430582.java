    public void check(final SensorHttpRequest request, final HttpClientListener listener) throws UnknownHostException {
        final URL url = request.getUrl();
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(params, DEFAULT_CONNECTION_TIMEOUT * 1000);
        HttpConnectionParams.setConnectionTimeout(params, DEFAULT_CONNECTION_TIMEOUT * 1000);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (compatible; Leemba/" + Config.VERSION + "; +http://www.leemba.com)");
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setHttpElementCharset(params, "UTF-8");
        DefaultHttpClient client = new DefaultHttpClient(params);
        SSLSocketFactory sf = null;
        for (String contextName : SSL_CONTEXT_NAMES) {
            try {
                SSLContext sslContext = SSLContext.getInstance(contextName);
                sslContext.init(null, new TrustManager[] { new TrustingManager() }, null);
                sf = new SSLSocketFactory(sslContext);
                break;
            } catch (NoSuchAlgorithmException e) {
                log.debug("SSLContext algorithm not available: " + contextName);
            } catch (Exception e) {
                log.debug("SSLContext can't be initialized: " + contextName, e);
            }
        }
        if (sf != null) {
            sf.setHostnameVerifier(new DummyX509HostnameVerifier());
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));
        } else log.error("No valid SSLContext found for https");
        HttpUriRequest req = null;
        if (request.method.equalsIgnoreCase("HEAD")) req = new HttpHead(url.toString()); else if (request.method.equalsIgnoreCase("POST")) req = new HttpPost(url.toString()); else req = new HttpGet(url.toString());
        if (request.getBasicAuthUsername() != null) {
            client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(request.getBasicAuthUsername(), request.getBasicAuthPassword()));
        }
        req.addHeader("Connection", "close");
        final StopWatch watch = new StopWatch().start();
        try {
            HttpResponse result = client.execute(req);
            ClientHttpResponse cr = new ClientHttpResponse();
            cr.setDuration((int) watch.stop());
            cr.setStatus(result.getStatusLine().getStatusCode());
            String charset = "UTF-8";
            HttpEntity entity = result.getEntity();
            if (entity != null && entity.getContentEncoding() != null) entity.getContentEncoding().getValue();
            cr.setMessage(result.getStatusLine().getStatusCode() + " - " + result.getStatusLine().getReasonPhrase());
            if (entity != null) {
                InputStream in = result.getEntity().getContent();
                byte buf[] = new byte[2048];
                int count = 0;
                StringWriter writer = new StringWriter();
                for (int total = 0; (count = in.read(buf)) > 0 && total < MAX_BODY; total += count) writer.write(new String(buf, 0, count, charset));
                in.close();
                String body = writer.toString();
                cr.setBody(body.replace("\r\n", "\n"));
            }
            listener.responseReceived(cr);
        } catch (Throwable t) {
            listener.exceptionCaught(t);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
