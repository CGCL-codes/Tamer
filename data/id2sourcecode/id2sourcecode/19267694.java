    public synchronized PageEntity loadPage(final String pageUrl) {
        HttpGet httpUriRequest = new HttpGet(pageUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            HttpResponse httpResponse = myHttpClient.execute(httpUriRequest);
            if (httpResponse.getStatusLine().getStatusCode() != HTTP_OK) {
                if (LogBridge.isLoggable()) LogBridge.w("Invalid statuscode for: " + pageUrl);
                return null;
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            httpEntity.writeTo(baos);
            String eTag = httpResponse.getFirstHeader(ETAG_HEADER).getValue();
            String htmlData = baos.toString(DEFAULT_ENCODING);
            if (htmlData.equals("")) {
                if (LogBridge.isLoggable()) LogBridge.w("Empty responsebody for: " + pageUrl);
                return null;
            }
            return new PageEntity(pageUrl, htmlData, eTag);
        } catch (ClientProtocolException e) {
            if (LogBridge.isLoggable()) LogBridge.w("Failed to load page: " + e.getMessage());
        } catch (IOException e) {
            if (LogBridge.isLoggable()) LogBridge.w("Failed to load page: " + e.getMessage());
        }
        return null;
    }
