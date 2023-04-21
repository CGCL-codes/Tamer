    public String sp(String url, List<NameValuePair> parametros) {
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
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                return "Status: " + status.getStatusCode();
            }
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            content = new ByteArrayOutputStream();
            readBytes = 0;
            descargaNormal.run();
            return new String(content.toByteArray());
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
            return "Error: " + e.getLocalizedMessage();
        }
    }
