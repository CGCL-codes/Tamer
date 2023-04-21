    private HashSet<String> getEntriesFromJar(URL url, String searchPrefix, String searchExtension) throws Exception {
        HashSet<String> ret = new HashSet<String>();
        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
        JarFile jarfile = jarConnection.getJarFile();
        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            int pathIndex = name.lastIndexOf('/');
            String path = name.substring(0, pathIndex + 1);
            if (path.equals(searchPrefix)) {
                String shortName = name.substring(pathIndex + 1);
                if (shortName.endsWith(searchExtension)) {
                    ret.add(shortName);
                }
            }
        }
        return ret;
    }
