    public static String httpGet(String url, boolean logStdout) throws Throwable {
        final int bufferSize = 4096;
        BufferedReader br = null;
        HttpURLConnection urlc = null;
        StringBuilder buffer = new StringBuilder();
        URL page = new URL(url);
        try {
            if (logStdout) System.err.println("Waiting for reply...:" + url);
            urlc = (HttpURLConnection) page.openConnection();
            urlc.setUseCaches(false);
            if (logStdout) {
                System.err.println("Content-type = " + urlc.getContentType());
                System.err.println("Content-length = " + urlc.getContentLength());
                System.err.println("Response-code = " + urlc.getResponseCode());
                System.err.println("Response-message = " + urlc.getResponseMessage());
            }
            int retCode = urlc.getResponseCode();
            String retMsg = urlc.getResponseMessage();
            if (retCode >= 400) throw new Throwable("HTTP Error: " + retCode + " - " + retMsg + " - URL:" + url);
            br = new BufferedReader(new InputStreamReader(urlc.getInputStream()), bufferSize);
            char buf[] = new char[bufferSize];
            int bytesRead = 0;
            while (bytesRead != -1) {
                bytesRead = br.read(buf);
                if (bytesRead > 0) buffer.append(buf, 0, bytesRead);
            }
            if (logStdout) {
                System.err.println("Document received.");
            }
            return buffer.toString();
        } catch (Throwable e) {
            throw e;
        } finally {
            if (br != null) br.close();
            if (urlc != null) urlc.disconnect();
        }
    }
