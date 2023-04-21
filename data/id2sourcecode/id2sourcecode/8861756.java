    public static URL[] search(ClassLoader cl, String prefix, String suffix) throws IOException {
        Enumeration e = cl.getResources(prefix);
        Set all = new HashSet();
        URL url;
        URLConnection conn;
        JarFile jarFile;
        while (e.hasMoreElements()) {
            url = (URL) e.nextElement();
            conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            if (conn instanceof JarURLConnection) {
                jarFile = ((JarURLConnection) conn).getJarFile();
            } else {
                jarFile = getAlternativeJarFile(url);
            }
            if (jarFile != null) {
                searchJar(cl, all, jarFile, prefix, suffix);
            } else {
                searchDir(all, new File(url.getFile()), suffix);
            }
        }
        URL[] urlArray = (URL[]) all.toArray(new URL[all.size()]);
        return urlArray;
    }
