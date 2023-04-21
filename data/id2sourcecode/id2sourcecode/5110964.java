    private void assertContentEquals(File expected, File actual) throws IOException {
        assertEquals(expected.length(), actual.length());
        InputStream expectedStream = new FileInputStream(expected);
        InputStream actualStream = new FileInputStream(actual);
        try {
            assertArrayEquals(IOUtil.toByteArray(expectedStream), IOUtil.toByteArray(actualStream));
        } finally {
            IOUtil.closeAndIgnoreErrors(expectedStream, actualStream);
        }
    }
