    private void assertFileEqualsToUrl(File file) throws Exception {
        assertStreamsEqual(new FileInputStream(file), new URL(url).openStream());
    }
