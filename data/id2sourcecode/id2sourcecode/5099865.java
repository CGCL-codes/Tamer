    private long getLastModified(URL url) {
        long result = 0;
        if (url != null) {
            try {
                URLConnection conn = url.openConnection();
                conn.connect();
                result = conn.getLastModified();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
