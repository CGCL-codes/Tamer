    public AudioClipImpl(URL url) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkConnect(url.getHost(), url.getPort());
            try {
                sm.checkPermission(url.openConnection().getPermission());
            } catch (IOException e) {
            }
        }
        this.url = url;
    }
