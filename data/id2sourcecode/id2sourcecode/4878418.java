    protected static InputStream tryURL(String contextPath, String path) {
        try {
            URL ctx = new URL(contextPath);
            URL url = new URL(ctx, path.substring(1));
            InputStream is = url.openStream();
            return is;
        } catch (IOException e) {
            return null;
        }
    }
