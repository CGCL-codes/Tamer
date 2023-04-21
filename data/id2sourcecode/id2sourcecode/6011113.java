    static String[] scanJars(String dir, String namespaceURL) throws IOException {
        String[] location = null;
        Iterator paths = Arrays.asList(new File(dir).listFiles()).iterator();
        while (paths.hasNext()) {
            String path = ((File) paths.next()).getCanonicalPath();
            if (!path.endsWith(".jar")) {
                continue;
            }
            URL url = new URL("jar:file:" + path + "!/");
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            location = scanJar(connection, namespaceURL);
            if (null != location) {
                return location;
            }
        }
        return null;
    }
