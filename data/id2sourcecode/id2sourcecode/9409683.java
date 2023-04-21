    @Test
    public void testFindResource() throws IllegalArgumentException, IOException {
        ExternalJarClassLoader cl = new ExternalJarClassLoader();
        cl.registerJar(BASE_EXTENSION_JAR);
        URL url = cl.findResource(RESOURCE_PATH);
        assertNotNull(url);
        Properties urlContent = new Properties();
        urlContent.load(url.openStream());
        assertTrue(urlContent.containsKey(RESOURCE_KEY));
    }
