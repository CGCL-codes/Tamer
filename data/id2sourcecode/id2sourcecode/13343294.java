    public static URL[] search(ClassLoader cl, String prefix, String suffix) throws IOException {
        Enumeration[] e = new Enumeration[] { cl.getResources(prefix), cl.getResources(prefix + "MANIFEST.MF") };
        Set all = new LinkedHashSet();
        URL url;
        URLConnection conn;
        JarFile jarFile;
        for (int i = 0, s = e.length; i < s; ++i) {
            while (e[i].hasMoreElements()) {
                url = (URL) e[i].nextElement();
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
                    searchDir(all, new File(URLDecoder.decode(url.getFile(), "UTF-8")), suffix);
                }
            }
        }
        URL[] urlArray = (URL[]) all.toArray(new URL[all.size()]);
        return urlArray;
    }
