    @Test
    public void testCsvUtils() throws BundledException {
        final File basePath = fileManager.createAutoDeletableTempDirectory("csvDir", ".dir");
        final File folderPathTest = new File(basePath, "Famille");
        folderPathTest.mkdirs();
        final File filePathTest = new File(folderPathTest, "1_2007-12-11.csv");
        final File readmeFilePath = new File(basePath, "readme.txt");
        csvUtils.writeCsvHeaders(filePathTest, Arrays.asList(COLMUNS), Arrays.asList(COMMENTS));
        assertEquals(Arrays.asList(csvManager.parseCsv(filePathTest)[0]), Arrays.asList(COLMUNS));
        csvUtils.writeReadmeTexts(readmeFilePath, Arrays.asList(README_TEXTS));
        fileManager.deleteRecursively(basePath);
    }
