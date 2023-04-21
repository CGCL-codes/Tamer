    public InputStream getInputStream() {
        try {
            String p = getPath();
            URL url = new URL(p);
            return url.openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
