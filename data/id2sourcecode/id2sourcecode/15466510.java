    private void testReaders(String path, MainArgs args) throws IOException {
        FileUtils.writeStringToFile(new File(path), "fread test\n");
        Reader fread = args.getReader("fread", path);
        assertNotNull(fread, "fread");
        BufferedReader b = new BufferedReader(fread);
        assertEquals(b.readLine(), "fread test", "fread contents");
        b.close();
        PipedInputStream pis = new PipedInputStream();
        PrintStream pos = new PrintStream(new PipedOutputStream(pis));
        System.setIn(pis);
        InputStreamReader sread = (InputStreamReader) args.getReader("sread", (Reader) null);
        assertNotNull(sread, "sread not null");
        String testString = "sread test";
        pos.println(testString);
        pos.close();
        BufferedReader bin = new BufferedReader(sread);
        assertEquals(bin.readLine(), testString, "sread string");
        Reader noReader = args.getReader("nread", fread);
        assertSame(noReader, fread, "nread");
        assertNotNull(args.getReader("nread", "-"), "nread");
        assertNotNull(args.getReader("nread", path), "nread");
        assertSame(args.getReader("nread", (String) null), null, "nread");
        assertSame(args.getReader("nread", (Reader) null), null, "nread");
        try {
            args.getReader("bread", (String) null);
            fail("bread did not fail");
        } catch (IOException ignored) {
        }
        try {
            args.getReader("bread", (Reader) null);
            fail("bread did not fail");
        } catch (IOException ignored) {
        }
    }
