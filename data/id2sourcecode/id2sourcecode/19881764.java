    public InputStream getDownloadStream(String directory) {
        String dl_url = getDownloadURL();
        if (dl_url == null || !isActive()) return null;
        InputStream in = null;
        try {
            if (dl_url.indexOf("://") != -1) {
                URL url = new URL(dl_url);
                in = url.openStream();
            } else {
                File file = getDownloadFile(directory);
                if (file == null) return null;
                in = new FileInputStream(file);
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, dl_url, ex);
            return null;
        }
        return in;
    }
