    public InputStream doMethod(URL url, String method, String messageBody) {
        logRequest(url, method, messageBody);
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Authorization", "Basic " + credentials);
            con.setRequestProperty("Content-Type", "application/xml;charset=utf-8");
            if (messageBody != null) {
                con.setDoOutput(true);
                byte[] body = messageBody.getBytes("utf-8");
                con.setRequestProperty("Content-Length", String.valueOf(body.length));
                OutputStream os = con.getOutputStream();
                os.write(body);
            }
            int responseCode = con.getResponseCode();
            logResponseCode(responseCode);
            if (!successful(responseCode)) {
                ByteBuffer buffer = new ByteBuffer(con.getErrorStream());
                logResponse(buffer);
                String response = buffer.getAsString("utf-8");
                String message = DOMUtils.stripXML(response);
                throw new ApiException(responseCode, message);
            } else {
                if (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE")) {
                    return null;
                } else {
                    ByteBuffer buffer = new ByteBuffer(con.getInputStream());
                    logResponse(buffer);
                    return buffer.getInputStream();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
