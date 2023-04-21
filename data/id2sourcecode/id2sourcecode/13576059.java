    static void check(URL url) throws IOException {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            URLConnection urlConnection = url.openConnection();
            Permission perm = urlConnection.getPermission();
            if (perm != null) {
                try {
                    security.checkPermission(perm);
                } catch (SecurityException se) {
                    if ((perm instanceof java.io.FilePermission) && perm.getActions().indexOf("read") != -1) {
                        security.checkRead(perm.getName());
                    } else if ((perm instanceof java.net.SocketPermission) && perm.getActions().indexOf("connect") != -1) {
                        URL locUrl = url;
                        if (urlConnection instanceof JarURLConnection) {
                            locUrl = ((JarURLConnection) urlConnection).getJarFileURL();
                        }
                        security.checkConnect(locUrl.getHost(), locUrl.getPort());
                    } else {
                        throw se;
                    }
                }
            }
        }
    }
