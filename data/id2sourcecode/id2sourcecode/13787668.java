    public static InputStream getInputStream(String filename) {
        try {
            InputStream in = null;
            java.net.URL url = null;
            try {
                url = new java.net.URL(filename);
                in = url.openStream();
            } catch (java.net.MalformedURLException e) {
                in = new FileInputStream(filename);
            }
            return in;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
