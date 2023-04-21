    public JSONObject verifyResponse(String requestUri, String postBody) {
        log.fine("verifyResponse:\nrequestUri = [" + requestUri + "]\npostBody = [" + postBody + "]");
        JSONObject result = new JSONObject();
        try {
            URL url = new URL(VERIFY_URL + this.developerKey);
            String postData = buildPostData(requestUri, postBody).toString();
            log.fine("verifyResponse postData:\n" + postData);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setRequestProperty("Content-Type", "application/json");
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.getOutputStream().write(postData.getBytes());
            httpurlconnection.getOutputStream().flush();
            httpurlconnection.getOutputStream().close();
            String content = Utils.streamToString(httpurlconnection.getInputStream(), httpurlconnection.getContentEncoding());
            log.fine("verifyResponse return: " + content);
            try {
                JSONArray response = new JSONArray(content);
                if (response != null && response.length() > 0) {
                    JSONObject ret = response.getJSONObject(0);
                    result = convertJson(ret);
                }
            } catch (JSONException e) {
                log.severe(e.getMessage());
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        } catch (RuntimeException e) {
            log.severe(e.getMessage());
        }
        return result;
    }
