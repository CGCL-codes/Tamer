    @Test
    public void test() throws Exception {
        final URL url = new URL("http://localhost:9095/hello/world");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            Assert.assertEquals("Hello world", reader.readLine());
            Assert.assertNull(reader.readLine());
        } finally {
            reader.close();
        }
    }
