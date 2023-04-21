    private final long getLastModified(String uri) {
        try {
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            long timestamp = connection.getLastModified();
            if (timestamp == 0) {
                if ("file".equals(url.getProtocol())) {
                    File localfile = new File(URLDecoder.decode(url.getFile()));
                    timestamp = localfile.lastModified();
                }
            }
            return (timestamp);
        } catch (Exception e) {
            return (System.currentTimeMillis());
        }
    }
