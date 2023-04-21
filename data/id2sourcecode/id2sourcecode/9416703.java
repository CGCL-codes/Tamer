    private String send(String endpoint, List<NameValuePair> nameValuePairs) {
        String ret = null;
        try {
            final UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            final HttpPost httpPost = new HttpPost(_serverUri + endpoint);
            httpPost.setEntity(urlEncodedFormEntity);
            final HttpResponse httpResponse = _httpClient.execute(httpPost);
            final StatusLine statusLine = httpResponse.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                final HttpEntity entity = httpResponse.getEntity();
                ret = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error while contacting the server");
        }
        return ret;
    }
