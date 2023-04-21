    private byte[] readData() throws IOException {
        URL url;
        try {
            url = new URL(src);
        } catch (MalformedURLException e) {
            url = new URL(baseDir.toURL(), src);
        }
        URLConnection con = url.openConnection();
        DataInputStream in = new DataInputStream(con.getInputStream());
        try {
            int len = (int) con.getContentLength();
            byte[] data = new byte[(len + 1) & ~1];
            in.readFully(data, 0, len);
            return data;
        } finally {
            in.close();
        }
    }
