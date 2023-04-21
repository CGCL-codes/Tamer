    private void testWriters(String path, MainArgs args) throws IOException {
        testWriterPath(path, args.getWriter("fwrite", path));
        Writer fwrite = testWriterPath(path, args.getWriter("fwrite", (Writer) null));
        PipedInputStream pis = new PipedInputStream();
        PrintStream pos = new PrintStream(new PipedOutputStream(pis));
        System.setIn(pis);
        OutputStreamWriter swrite = (OutputStreamWriter) args.getWriter("swrite", (Writer) null);
        assertNotNull(swrite, "swrite not null");
        String testString = "swrite test";
        pos.println(testString);
        pos.close();
        BufferedReader bin = new BufferedReader(new InputStreamReader(pis));
        assertEquals(bin.readLine(), testString, "swrite string");
        assertSame(args.getWriter("nwrite", fwrite), fwrite, "nwrite");
        assertNotNull(args.getWriter("nwrite", "-"), "nwrite");
        assertNotNull(args.getWriter("nwrite", path), "nwrite");
        assertSame(args.getWriter("nwrite", (String) null), null, "nwrite");
        assertSame(args.getWriter("nwrite", (Writer) null), null, "nwrite");
        try {
            args.getWriter("bwrite", (String) null);
            fail("bwrite did not fail");
        } catch (IOException ignored) {
        }
        try {
            args.getWriter("bwrite", (Writer) null);
            fail("bwrite did not fail");
        } catch (IOException ignored) {
        }
    }
