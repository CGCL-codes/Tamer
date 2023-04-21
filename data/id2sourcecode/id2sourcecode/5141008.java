    @Test
    public void testCopyFile() throws IOException {
        String inputFilePath = ApplicationLogger.getLogger().getPathLogFile();
        String outputFilePath = "./output.test";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        ApplicationLogger.getLogger().log(Level.SEVERE, "Log testCopyFile");
        FileUtils.copyFile(inputFile, outputFile);
        assertEquals(true, FileUtils.compareFiles(inputFile, outputFile));
    }
