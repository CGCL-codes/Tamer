    private static byte[] getBytes(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        if (uc instanceof java.net.HttpURLConnection) {
            java.net.HttpURLConnection huc = (java.net.HttpURLConnection) uc;
            int code = huc.getResponseCode();
            if (code >= java.net.HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new IOException("open HTTP connection failed.");
            }
        }
        int len = uc.getContentLength();
        InputStream in = new BufferedInputStream(uc.getInputStream());
        byte[] b;
        try {
            b = IOUtils.readFully(in, len, true);
        } finally {
            in.close();
        }
        return b;
    }
