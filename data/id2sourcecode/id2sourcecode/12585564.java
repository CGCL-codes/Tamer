    public static String downloadFileToString(String urlString) throws IOException {
        URLConnection urlConn = null;
        InputStream in = null;
        URL url = new URL(urlString);
        urlConn = url.openConnection();
        in = urlConn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            LFileCopy.copy(in, out);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
        return new String(out.toByteArray(), "UTF-8");
    }
