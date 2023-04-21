    public String sendPost(String url, List<NameValuePair> parametros) {
        cancelled = false;
        hasNewDialog = false;
        if (sUserAgent == null) {
            return "";
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new UrlEncodedFormEntity(parametros));
            request.setHeader("User-Agent", sUserAgent);
            client.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                return "" + status.getReasonPhrase();
            }
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            content = new ByteArrayOutputStream();
            readBytes = 0;
            descargaNormal.run();
            if (failed || cancelled) {
                return "";
            }
            return new String(content.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }
