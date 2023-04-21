    public final Map<URL, String> getClasspathLocations() {
        Map<URL, String> map = new TreeMap<URL, String>(URL_COMPARATOR);
        File file = null;
        String pathSep = System.getProperty("path.separator");
        String classpath = System.getProperty("java.class.path");
        StringTokenizer st = new StringTokenizer(classpath, pathSep);
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            file = new File(path);
            include(null, file, map);
        }
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            for (Enumeration e = cl.getResources("META-INF/"); e.hasMoreElements(); ) {
                URL url = (URL) e.nextElement();
                JarURLConnection jc = (JarURLConnection) url.openConnection();
                JarFile jf = jc.getJarFile();
                this.mapJAR(map, url, jf);
            }
        } catch (IOException ioe) {
        }
        return map;
    }
