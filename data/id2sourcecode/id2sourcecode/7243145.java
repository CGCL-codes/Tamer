    @Test
    public void testExceptionBoilerplate() throws IOException {
        File in = File.createTempFile("tmp", ".cml");
        FileUtils.copyFile(getResourceFile("empty.cml"), in);
        File out = File.createTempFile("tmp", ".cml");
        iaeFor(null, out);
        iaeFor(in, null);
        iaeFor(new File("."), out);
        iaeFor(in, new File("."));
    }
