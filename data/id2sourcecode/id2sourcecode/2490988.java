    public void copyTextOutputFileToExpected(final String outputFileName, final String expectedOutputFileName) {
        try {
            FileUtils.copyFile(outputFileName, expectedOutputFileName);
        } catch (final IOException e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = e.getClass().getName();
            }
            Assert.fail(msg);
        }
    }
