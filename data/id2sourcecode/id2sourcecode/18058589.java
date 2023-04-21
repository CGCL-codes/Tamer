    void verify() throws JarException, IOException {
        if (!savePerms) {
            return;
        }
        final URL url = jarURL.getProtocol().equalsIgnoreCase("jar") ? jarURL : new URL("jar:" + jarURL.toString() + "!/");
        JarFile jf = null;
        try {
            try {
                jf = (JarFile) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                    public Object run() throws Exception {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        conn.setUseCaches(false);
                        return conn.getJarFile();
                    }
                });
            } catch (java.security.PrivilegedActionException pae) {
                SecurityException se = new SecurityException("Cannot load " + url.toString());
                se.initCause(pae);
                throw se;
            }
            if (jf != null) {
                JarEntry je = jf.getJarEntry("cryptoPerms");
                if (je == null) {
                    throw new JarException("Can not find cryptoPerms");
                }
                try {
                    appPerms = new CryptoPermissions();
                    appPerms.load(jf.getInputStream(je));
                } catch (Exception ex) {
                    JarException jex = new JarException("Cannot load/parse" + jarURL.toString());
                    jex.initCause(ex);
                    throw jex;
                }
            }
        } finally {
            if (jf != null) {
                jf.close();
            }
        }
    }
