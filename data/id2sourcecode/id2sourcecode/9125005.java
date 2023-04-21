    private String get(URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            Manifest mf = new Manifest(in);
            return mf.getMainAttributes().getValue(expression);
        } finally {
            if (null != in) in.close();
        }
    }
