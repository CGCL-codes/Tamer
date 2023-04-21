    public InputStream getResourceAsStream(String fileName) {
        try {
            for (int i = 0; i < uri.length; i++) {
                URL url = new URL(uri[i].toURL(), fileName);
                URLConnection conn = url.openConnection();
                conn.setDefaultUseCaches(false);
                conn.setDoOutput(true);
                conn.setIfModifiedSince(0);
                InputStream is = conn.getInputStream();
                if (is != null) return is;
            }
        } catch (IOException ex) {
        }
        return null;
    }
