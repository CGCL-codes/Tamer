    private static boolean tryExpandPySrc(File f) throws GythonException {
        if (f.exists()) {
            if (!f.isDirectory() || !f.canWrite()) {
                return false;
            }
        } else {
            boolean dirOK = f.mkdirs();
        }
        if (f.exists() && f.isDirectory() && f.canWrite()) {
            java.net.URL url = Gython.class.getResource("jython");
            if (url == null) {
                throw new GythonException("cannot find python source code resources relative to Gython jar file");
            }
            java.net.URLConnection conn;
            try {
                conn = url.openConnection();
            } catch (IOException e) {
                String msg = "Error opening connection to " + url.toString();
                logger.error(msg, e);
                throw new GythonException("Error copying " + url.toString(), e);
            }
            if (conn == null) {
                throw new GythonException("cannot find python source code resources relative to Gython jar file");
            }
            if (conn instanceof java.net.JarURLConnection) {
                logger.debug("Expanding python source from jar file");
                try {
                    IOUtil.expandJar((java.net.JarURLConnection) conn, f);
                    return true;
                } catch (Exception e) {
                    throw new GythonException("Error expanding python source" + " from jar file at " + conn.getURL().toString() + ": " + e.getMessage());
                }
            } else {
                try {
                    IOUtil.copyDir(new File(url.getFile()), f);
                    return true;
                } catch (Exception e) {
                    throw new GythonException("Error expanding python source" + " from jar file at " + conn.getURL().toString() + ": " + e.getMessage());
                }
            }
        }
        return false;
    }
