    public void testReload() {
        String originalBasic = readTestFile("basic");
        try {
            testGet();
            writeToTestFile("basic", readTestFile("updated_basic"));
            assertEquals("Updated text response", doGetAndRead("basic"));
        } finally {
            writeToTestFile("basic", originalBasic);
        }
    }
