    private RandomAccessFile testRandomAccessFilePath(String path, RandomAccessFile frand) throws IOException {
        FileUtils.writeStringToFile(new File(path), "fread test\n");
        assertNotNull(frand, "frand");
        String s = FileUtils.readFileToString(new File(path));
        assertEquals(s, "fread test\n", "fread contents");
        return frand;
    }
