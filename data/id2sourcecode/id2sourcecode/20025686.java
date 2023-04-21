    private static void checkConnect(URL url) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                java.security.Permission perm = url.openConnection().getPermission();
                if (perm != null) security.checkPermission(perm); else security.checkConnect(url.getHost(), url.getPort());
            } catch (java.io.IOException ioe) {
                security.checkConnect(url.getHost(), url.getPort());
            }
        }
    }
