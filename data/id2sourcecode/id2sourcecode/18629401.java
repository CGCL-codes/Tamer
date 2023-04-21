    public void mySendPostJSON() {
        int TIMEOUT_MILLISEC = 10000;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost();
        try {
            JSONObject json = new JSONObject();
            json.put("login", "Mark");
            json.put("password", "test");
            logView.append("\n" + json.toString());
            StringEntity stringEntity = new StringEntity(json.toString());
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "application/json"));
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            logView.append("\n" + stringEntity.toString());
            URI uri = new URI(urlStr);
            httpPost.setURI(uri);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            InputStream jsonfile = httpResponse.getEntity().getContent();
            String response = convertStreamToString(jsonfile);
            logView.append("\n" + response);
            JSONObject JSONResponse = new JSONObject(response);
            logView.append("\n" + JSONResponse.get("auth_token") + "\n" + JSONResponse.get("status") + "\n" + JSONResponse.get("status_description"));
        } catch (Exception e) {
            logView.append("\n" + e.getMessage());
        }
    }
