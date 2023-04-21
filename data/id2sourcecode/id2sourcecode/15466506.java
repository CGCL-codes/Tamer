    private Writer testWriterPath(String path, Writer fwrite) throws IOException {
        assertNotNull(fwrite, "fwrite");
        fwrite.write("fwrite test\n");
        fwrite.close();
        BufferedReader in = new BufferedReader(new FileReader(path));
        assertEquals(in.readLine(), "fwrite test");
        return fwrite;
    }
