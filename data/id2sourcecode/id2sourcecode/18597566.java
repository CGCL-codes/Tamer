    public void check(final String fileName, final File refFolder, final File resultFolder) throws Exception {
        File resultFile = new File(resultFolder, fileName);
        String normalizedResult = normalizeFileLocations(FileUtils.readFileToString(resultFile));
        File normalizedResultFile = File.createTempFile("result", null, new File(System.getProperty("java.io.tmpdir")));
        normalizedResultFile.deleteOnExit();
        FileUtils.writeStringToFile(normalizedResultFile, normalizedResult);
        if (isCreateReferences()) {
            FileUtils.copyFile(normalizedResultFile, new File(refFolder, fileName));
        } else {
            File referenceFile = new File(refFolder, fileName);
            assertEquals(referenceFile, normalizedResultFile);
        }
    }
