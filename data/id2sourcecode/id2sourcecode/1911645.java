    protected void set(String key, String value) throws MalformedURLException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("Saving " + key + ": " + value);
        }
        URL myUrl = new URL(BASE_URL);
        HttpURLConnection urlConn = (HttpURLConnection) myUrl.openConnection();
        log.debug("Connection opened");
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
        byte[] bValue = URLEncoder.encode(value, "UTF-8").getBytes("UTF-8");
        log.debug("Sending request of size " + bValue.length);
        printout.write((key + "=" + URLEncoder.encode(value, "UTF-8")).getBytes("UTF-8"));
        printout.flush();
        printout.close();
        DataInputStream input = new DataInputStream(urlConn.getInputStream());
        log.debug("Reply recieved with code: " + urlConn.getResponseCode());
        if (urlConn.getResponseCode() != 200) {
            log.warn("Recieved error response of " + urlConn.getResponseCode() + " " + urlConn.getRequestMethod());
            throw new IOException("Invalid response from save " + urlConn.getResponseCode());
        }
        input.close();
        if (log.isTraceEnabled()) {
            log.trace("Save successful");
        }
    }
