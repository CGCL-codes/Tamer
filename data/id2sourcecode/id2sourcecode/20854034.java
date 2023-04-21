    protected byte[] getImage(String url, String sessionID, boolean logStdout) throws Throwable {
        HttpURLConnection urlc = null;
        BufferedInputStream bin = null;
        final int bufferSize = 10240;
        byte[] buffer = null;
        URL page = new URL(url);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        if (logStdout) System.err.println("Waiting for reply...:" + url);
        try {
            urlc = (HttpURLConnection) page.openConnection();
            urlc.setUseCaches(false);
            urlc.addRequestProperty("Host", getRequest().getServerName());
            urlc.addRequestProperty("Cookie", "JSESSIONID=" + sessionID);
            urlc.addRequestProperty("Cache-Control", "max-age=0");
            if (logStdout) {
                System.err.println("Content-type = " + urlc.getContentType());
                System.err.println("Content-length = " + urlc.getContentLength());
                System.err.println("Response-code = " + urlc.getResponseCode());
                System.err.println("Response-message = " + urlc.getResponseMessage());
            }
            int retCode = urlc.getResponseCode();
            String retMsg = urlc.getResponseMessage();
            if (retCode >= 400) throw new Throwable("HTTP Error: " + retCode + " - " + retMsg + " - URL:" + url);
            int size = urlc.getContentLength();
            if (size > 0) buffer = new byte[size]; else buffer = new byte[bufferSize];
            bin = new BufferedInputStream(urlc.getInputStream(), buffer.length);
            int bytesRead = 0;
            do {
                bytesRead = bin.read(buffer);
                if (bytesRead > 0) bout.write(buffer, 0, bytesRead);
            } while (bytesRead != -1);
            if (logStdout) {
                System.err.println("Connection closed.");
            }
            return bout.toByteArray();
        } catch (Throwable e) {
            throw e;
        } finally {
            if (bin != null) bin.close();
            if (urlc != null) urlc.disconnect();
        }
    }
