    protected synchronized InputStream getInputStream() {
        InputStream ips = null;
        try {
            URL url = new URL(this.getDataSource().getUrl());
            if ("file".equals(url.getProtocol())) ips = new FileInputStream(url.getFile()); else if ("http".equals(url.getProtocol())) ips = url.openStream(); else logger.warning("Impossible to load file from " + url + ". Unsupported protocol " + url.getProtocol());
        } catch (Exception e) {
            logger.warning("Impossible to read from: " + this.getDataSource().getUrl());
            e.printStackTrace();
        }
        return ips;
    }
