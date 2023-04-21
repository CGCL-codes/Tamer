    public void testFormatingAFileWithOverwriting() throws IOException, TableFormatError {
        FileWriter sampleFile = new FileWriter("test/com/fitagilifier/format/resource/SampleFitFileWorstOverwrite.html");
        sampleFile.write(FormatTestDataProvider.fileContentWorst);
        sampleFile.close();
        Format.fitFile("test/com/fitagilifier/format/resource/SampleFitFileWorstOverwrite.html", "test/com/fitagilifier/format/resource/SampleFitFileWorstOverwrite.html");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        assertEquals("Fit file must be well formated", FormatTestDataProvider.fileContentWorstFormated, FileUtils.readFileToString(new File("test/com/fitagilifier/format/resource/SampleFitFileWorstOverwrite.html")));
    }
