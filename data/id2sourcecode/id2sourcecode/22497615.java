    public String getResponseViaProxy(String url, String proxyHost, String proxyPort, String proxyProtocol) {
        HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort), proxyProtocol);
        HttpClient httpClient = new DefaultHttpClient();
        String responseBody = null;
        try {
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpGet httpget = new HttpGet(url);
            log.info("executing request to " + httpget.getURI() + " via " + proxy);
            HttpResponse rsp = httpClient.execute(httpget);
            HttpEntity entity = rsp.getEntity();
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
    }
