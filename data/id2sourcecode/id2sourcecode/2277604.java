    public URLDecodingImageSource(URL url) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkConnect(url.getHost(), url.getPort());
            try {
                Permission p = url.openConnection().getPermission();
                security.checkPermission(p);
            } catch (IOException e) {
            }
        }
        this.url = url;
    }
