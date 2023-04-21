    @SuppressWarnings("unused")
    private HttpEntity httpRequest(String method, String url, Map<String, String> header, String body) throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpRequestBase request;
        if (method.equals("GET")) {
            request = new HttpGet(url);
        } else if (method.equals("POST")) {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("text", new StringBody(body));
            request = new HttpPost(url);
            ((HttpPost) request).setEntity(entity);
        } else {
            throw new Exception("Invalid request method");
        }
        Set<String> keys = header.keySet();
        for (String key : keys) {
            request.setHeader(key, header.get(key));
        }
        logger.info("Request: " + request.getRequestLine());
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity entity = httpResponse.getEntity();
        String responseContent = EntityUtils.toString(entity);
        if (trafficListener != null) {
            TrafficItem trafficItem = new TrafficItem();
            trafficItem.setRequest(request);
            trafficItem.setResponse(httpResponse);
            trafficItem.setResponseContent(responseContent);
            trafficListener.handleRequest(trafficItem);
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (!(statusCode >= 200 && statusCode < 300)) {
            JOptionPane.showMessageDialog(null, responseContent);
            throw new Exception("No successful status code");
        }
        return entity;
    }
