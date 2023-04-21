    static String getContentType(String fileName) throws IOException {
        String resourceName = "org/apache/harmony/luni/tests/" + fileName;
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        assertNotNull("Cannot find test resource " + resourceName, url);
        return url.openConnection().getContentType();
    }
