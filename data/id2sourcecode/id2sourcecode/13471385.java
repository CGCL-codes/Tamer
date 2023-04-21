    private JarFile getJarFile(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        JarFile jarFile;
        if (conn instanceof JarURLConnection) {
            jarFile = ((JarURLConnection) conn).getJarFile();
        } else {
            jarFile = _getAlternativeJarFile(url);
        }
        return jarFile;
    }
