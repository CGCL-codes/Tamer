    private void includeJar(File file, Map map) {
        if (file.isDirectory()) return;
        URL jarURL = null;
        JarFile jar = null;
        try {
            String canonicalPath = file.getCanonicalPath();
            if (!canonicalPath.startsWith("/")) {
                canonicalPath = "/" + canonicalPath;
            }
            jarURL = new URL("file:" + canonicalPath);
            jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
            URLConnection urlConnection = jarURL.openConnection();
            JarURLConnection conn = (JarURLConnection) urlConnection;
            jar = conn.getJarFile();
        } catch (Exception e) {
            return;
        }
        if (jar == null || jarURL == null) return;
        map.put(jarURL, "");
        Enumeration e = jar.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = (JarEntry) e.nextElement();
            if (entry.isDirectory()) {
                if (entry.getName().toUpperCase().equals("META-INF/")) continue;
                try {
                    map.put(new URL(jarURL.toExternalForm() + entry.getName()), packageNameFor(entry));
                } catch (MalformedURLException murl) {
                    continue;
                }
            }
        }
    }
