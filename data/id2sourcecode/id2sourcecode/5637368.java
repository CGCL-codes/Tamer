    public ConfigProperties(URL url) throws IOException {
        InputStream in = null;
        try {
            load(in = url.openStream());
        } catch (Exception e) {
            throw new IOException("Could not load configuration from " + url);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
