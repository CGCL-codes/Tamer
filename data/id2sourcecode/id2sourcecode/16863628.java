    public String getJarFileName(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        if (conn instanceof JarURLConnection) {
            JarURLConnection jarConn = (JarURLConnection) conn;
            return jarConn.getJarFile().getName();
        }
        return null;
    }
