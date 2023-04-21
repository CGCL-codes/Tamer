    public static String loadVersion(String groupId, String artifactId) {
        URL url = PomUtils.class.getResource("/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
        if (url == null) return "SNAPSHOT";
        Properties p = new Properties();
        try {
            InputStream in = url.openStream();
            p.load(in);
            return p.getProperty("version");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + url, e);
        }
    }
