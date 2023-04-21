    public void testBasic() throws IOException {
        Set fileExtensions = new HashSet();
        fileExtensions.add("fdt");
        fileExtensions.add("fdx");
        Directory primaryDir = new MockRAMDirectory();
        RAMDirectory secondaryDir = new MockRAMDirectory();
        FileSwitchDirectory fsd = new FileSwitchDirectory(fileExtensions, primaryDir, secondaryDir, true);
        IndexWriter writer = new IndexWriter(fsd, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.LIMITED);
        writer.setUseCompoundFile(false);
        TestIndexWriterReader.createIndexNoClose(true, "ram", writer);
        IndexReader reader = writer.getReader();
        assertEquals(100, reader.maxDoc());
        writer.commit();
        String[] files = primaryDir.listAll();
        assertTrue(files.length > 0);
        for (int x = 0; x < files.length; x++) {
            String ext = FileSwitchDirectory.getExtension(files[x]);
            assertTrue(fileExtensions.contains(ext));
        }
        files = secondaryDir.listAll();
        assertTrue(files.length > 0);
        for (int x = 0; x < files.length; x++) {
            String ext = FileSwitchDirectory.getExtension(files[x]);
            assertFalse(fileExtensions.contains(ext));
        }
        reader.close();
        writer.close();
        files = fsd.listAll();
        for (int i = 0; i < files.length; i++) {
            assertNotNull(files[i]);
        }
        fsd.close();
    }
