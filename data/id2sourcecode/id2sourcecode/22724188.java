    public void testCopyFile() throws Exception {
        File first = File.createTempFile("unitTest", ".txt");
        File second = File.createTempFile("unitTestCopy", ".txt");
        first.deleteOnExit();
        second.deleteOnExit();
        FileWriter writer = new FileWriter(first);
        writer.write("Mary had a little lamb");
        writer.close();
        FileUtils.copyFile(first, second);
        assertEquals("File sizes ", first.length(), second.length());
    }
