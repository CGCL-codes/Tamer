    private Object getRepresentationInternal(long now) throws Exception {
        URL url;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            url = null;
        }
        long newLastModified;
        URLConnection conn;
        if (url != null) {
            if ("file".equals(url.getProtocol())) {
                newLastModified = resource.getFile().lastModified();
                conn = null;
            } else {
                conn = url.openConnection();
                newLastModified = conn.getLastModified();
            }
        } else {
            newLastModified = 0;
            conn = null;
        }
        lastChecked = now;
        if (representation == null || newLastModified != lastModified) {
            lastModified = newLastModified;
            InputStream in = conn == null ? resource.getInputStream() : conn.getInputStream();
            try {
                representation = loadRepresentation(in);
            } finally {
                in.close();
            }
        } else if (conn != null) {
            conn.getInputStream().close();
        }
        return representation;
    }
