    public boolean sendNoRetry(String registrationId, String collapse, Map<String, String[]> params, boolean delayWhileIdle) throws IOException {
        StringBuilder postDataBuilder = new StringBuilder();
        postDataBuilder.append(PARAM_REGISTRATION_ID).append("=").append(registrationId);
        if (delayWhileIdle) {
            postDataBuilder.append("&").append(PARAM_DELAY_WHILE_IDLE).append("=1");
        }
        postDataBuilder.append("&").append(PARAM_COLLAPSE_KEY).append("=").append(collapse);
        for (Object keyObj : params.keySet()) {
            String key = (String) keyObj;
            if (key.startsWith("data.")) {
                String[] values = (String[]) params.get(key);
                postDataBuilder.append("&").append(key).append("=").append(URLEncoder.encode(values[0], UTF8));
            }
        }
        byte[] postData = postDataBuilder.toString().getBytes(UTF8);
        URL url = new URL(C2DM_SEND_ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
        String authToken = serverConfig.getToken();
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpServletResponse.SC_UNAUTHORIZED || responseCode == HttpServletResponse.SC_FORBIDDEN) {
            log.warning("Unauthorized - need token");
            serverConfig.invalidateCachedToken();
            return false;
        }
        String updatedAuthToken = conn.getHeaderField(UPDATE_CLIENT_AUTH);
        if (updatedAuthToken != null && !authToken.equals(updatedAuthToken)) {
            log.info("Got updated auth token from C2DM servers: " + updatedAuthToken);
            serverConfig.updateToken(updatedAuthToken);
        }
        String responseLine = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
        log.info("Got " + responseCode + " response from Google C2DM endpoint.");
        if (responseLine == null || responseLine.equals("")) {
            throw new IOException("Got empty response from Google C2DM endpoint.");
        }
        String[] responseParts = responseLine.split("=", 2);
        if (responseParts.length != 2) {
            log.warning("Invalid message from google: " + responseCode + " " + responseLine);
            throw new IOException("Invalid response from Google " + responseCode + " " + responseLine);
        }
        if (responseParts[0].equals("id")) {
            log.info("Successfully sent data message to device: " + responseLine);
            return true;
        }
        if (responseParts[0].equals("Error")) {
            String err = responseParts[1];
            log.warning("Got error response from Google C2DM endpoint: " + err);
            throw new IOException("Server error: " + err);
        } else {
            log.warning("Invalid response from google " + responseLine + " " + responseCode);
            return false;
        }
    }
