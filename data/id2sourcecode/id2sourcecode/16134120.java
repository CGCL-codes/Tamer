    static ByteArrayInputStream getStream(String url) throws IOException, MalformedURLException {
        InputStream in = new URL(url).openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream(256);
        int result;
        byte[] buf = new byte[256];
        while ((result = in.read(buf)) != -1) {
            out.write(buf, 0, result);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
