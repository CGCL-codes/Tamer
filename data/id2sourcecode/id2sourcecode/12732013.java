    public static java.io.InputStream getInputStream(String ref) throws java.io.IOException {
        if (ref == null || ref.length() == 0) {
            throw new java.net.MalformedURLException("resource null or empty");
        } else if (ref.length() > 10 && ref.substring(0, 9).toUpperCase().equals("RESOURCE:")) {
            String resource = ref.substring(9, ref.length());
            try {
                return ClassLoader.getSystemResourceAsStream(resource);
            } catch (Exception ex) {
                throw new java.io.IOException(resource);
            }
        } else {
            java.io.File f = new java.io.File(ref);
            if (f.isFile() && f.canRead()) {
                try {
                    return new java.io.FileInputStream(f);
                } catch (java.io.IOException ioe) {
                    throw ioe;
                }
            }
            java.io.InputStream is = ioh.getClass().getResourceAsStream(ref);
            if (is != null) return is;
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(ref);
            if (is != null) return is;
            try {
                java.net.URL url = new java.net.URL(ref);
                try {
                    return url.openStream();
                } catch (java.io.IOException ioe) {
                    throw ioe;
                }
            } catch (java.net.MalformedURLException mue) {
                throw mue;
            }
        }
    }
