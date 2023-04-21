    private InputStream getSourceInputStream() {
        InputStream in = null;
        if (url != null) {
            try {
                return url.openStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (path != null) {
            try {
                return new FileInputStream(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return in;
    }
