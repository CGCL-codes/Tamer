    @Override
    public InputStream openStream() {
        if (in != null) {
            InputStream in2 = in;
            in = null;
            return in2;
        } else {
            try {
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                return conn.getInputStream();
            } catch (IOException ex) {
                throw new AssetLoadException("Failed to read URL " + url, ex);
            }
        }
    }
