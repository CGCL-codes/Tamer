    public static void returnURL(URL url, OutputStream out) throws IOException {
        InputStream in = url.openStream();
        byte[] buf = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = in.read(buf)) != -1) {
            out.write(buf, 0, bytesRead);
        }
    }
