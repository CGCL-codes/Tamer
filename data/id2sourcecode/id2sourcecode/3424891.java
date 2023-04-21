    public static InputStream doPost(final URL url, final Map<String, Object> parameters, final boolean encode) throws IOException {
        final URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream os = null;
        try {
            os = new DataOutputStream(urlConn.getOutputStream());
            os.write(getQueryString(parameters, encode).getBytes());
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
        return urlConn.getInputStream();
    }
