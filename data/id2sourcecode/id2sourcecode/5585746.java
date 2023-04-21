    public Image createImage(URL url) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                java.security.Permission perm = url.openConnection().getPermission();
                if (perm != null) {
                    try {
                        sm.checkPermission(perm);
                    } catch (SecurityException se) {
                        if ((perm instanceof java.io.FilePermission) && perm.getActions().indexOf("read") != -1) {
                            sm.checkRead(perm.getName());
                        } else if ((perm instanceof java.net.SocketPermission) && perm.getActions().indexOf("connect") != -1) {
                            sm.checkConnect(url.getHost(), url.getPort());
                        } else {
                            throw se;
                        }
                    }
                }
            } catch (java.io.IOException ioe) {
                sm.checkConnect(url.getHost(), url.getPort());
            }
        }
        return createImage(new URLImageSource(url));
    }
