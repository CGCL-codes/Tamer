    protected void checkAttributes() {
        exists = false;
        editable = false;
        isUrl = false;
        if (path != null) {
            if (path.startsWith("http:")) {
                isUrl = true;
                try {
                    java.net.URL url = new java.net.URL(path);
                    java.net.URLConnection con = url.openConnection();
                    exists = (con != null);
                } catch (Exception ex) {
                }
            } else {
                java.io.File file = new java.io.File(path);
                exists = file.exists() && !file.isDirectory() && file.canRead();
                if (exists) editable = file.canWrite();
            }
        }
    }
