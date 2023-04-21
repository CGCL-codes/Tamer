    protected byte[] loadByteCode(String u) throws IOException {
        InputStream in;
        URL url;
        in = null;
        try {
            url = new URL(u);
            in = url.openStream();
            return loadByteCode(in);
        } catch (MalformedURLException e) {
            throw new IOException("Bad URL syntax: \"" + u + "\"");
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
