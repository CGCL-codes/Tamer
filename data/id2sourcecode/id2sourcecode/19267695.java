    public synchronized boolean isPageModified(final String pageUrl, final String eTag) {
        HttpHead httpUriRequest = new HttpHead(pageUrl);
        try {
            HttpResponse httpResponse = myHttpClient.execute(httpUriRequest);
            if (httpResponse.getStatusLine().getStatusCode() != HTTP_OK) {
                if (LogBridge.isLoggable()) LogBridge.w("Invalid statuscode for: " + pageUrl);
                return false;
            }
            Header eTagHeader = httpResponse.getFirstHeader(ETAG_HEADER);
            if (eTagHeader != null && eTagHeader.getValue().equals(eTag)) {
                if (LogBridge.isLoggable()) LogBridge.i("Page not modified: " + pageUrl + " (" + eTag + ")");
                return true;
            }
        } catch (ClientProtocolException e) {
            if (LogBridge.isLoggable()) LogBridge.w("Failed to check page: " + e.getMessage());
        } catch (IOException e) {
            if (LogBridge.isLoggable()) LogBridge.w("Failed to check page: " + e.getMessage());
        }
        if (LogBridge.isLoggable()) LogBridge.i("Page is modified: " + pageUrl + "/ (" + eTag + ")");
        return false;
    }
