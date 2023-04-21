    private long getLastModified(URL url) {
        long lastModified = 0;
        try {
            URLConnection conn = url.openConnection();
            lastModified = conn.getLastModified();
        } catch (Exception e) {
        }
        if (lastModified == 0) {
            String filepath = url.getPath();
            if (filepath != null) {
                File f = new File(filepath);
                if (f.exists()) {
                    lastModified = f.lastModified();
                }
            }
        }
        return lastModified;
    }
