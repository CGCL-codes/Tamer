    public static JarURLConnection getJarURLConnection(URL url) throws IOException {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        return connection;
    }
