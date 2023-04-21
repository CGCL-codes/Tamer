    private void upload() throws IOException {
        System.clearProperty("javax.net.ssl.trustStoreProvider");
        System.clearProperty("javax.net.ssl.trustStoreType");
        final String BOUNDARY = "CowMooCowMooCowCowCow";
        URL url = createUploadURL();
        log("The upload URL is " + url);
        InputStream in = new BufferedInputStream(new FileInputStream(fileName));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (this.isIgnoreSslCertificateHostname()) {
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection secure = (HttpsURLConnection) conn;
                secure.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        boolean result = true;
                        log("SSL verification ignored for current session and hostname: " + hostname);
                        return result;
                    }
                });
            }
        }
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Basic " + createAuthToken(userName, password));
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("User-Agent", "Google Code Upload Ant Task 0.1");
        log("Attempting to connect (username is " + userName + ")...");
        conn.connect();
        log("Sending request parameters...");
        OutputStream out = conn.getOutputStream();
        sendLine(out, "--" + BOUNDARY);
        sendLine(out, "content-disposition: form-data; name=\"summary\"");
        sendLine(out, "");
        sendLine(out, summary);
        if (labels != null) {
            String[] labelArray = labels.split("\\,");
            if (labelArray != null && labelArray.length > 0) {
                log("Setting " + labelArray.length + " label(s)");
                for (int n = 0, i = labelArray.length; n < i; n++) {
                    sendLine(out, "--" + BOUNDARY);
                    sendLine(out, "content-disposition: form-data; name=\"label\"");
                    sendLine(out, "");
                    sendLine(out, labelArray[n].trim());
                }
            }
        }
        log("Sending file... " + targetFileName);
        sendLine(out, "--" + BOUNDARY);
        sendLine(out, "content-disposition: form-data; name=\"filename\"; filename=\"" + targetFileName + "\"");
        sendLine(out, "Content-Type: application/octet-stream");
        sendLine(out, "");
        int count;
        byte[] buf = new byte[8192];
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        in.close();
        sendLine(out, "");
        sendLine(out, "--" + BOUNDARY + "--");
        out.flush();
        out.close();
        in = conn.getInputStream();
        log("Upload finished. Reading response.");
        log("HTTP Response Headers: " + conn.getHeaderFields());
        StringBuilder responseBody = new StringBuilder();
        while ((count = in.read(buf)) >= 0) {
            responseBody.append(new String(buf, 0, count, "ascii"));
        }
        log(responseBody.toString());
        in.close();
        conn.disconnect();
    }
