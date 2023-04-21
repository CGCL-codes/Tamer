    private File createTestSpreadsheet(ArrayList stories) throws IOException {
        File tempFile = File.createTempFile("test ", "with " + stories.size() + " stories.xls");
        OutputStream out = new FileOutputStream(tempFile);
        SpreadsheetStoryWriter writer = new SpreadsheetStoryWriter(out);
        writer.writeStories(stories);
        return tempFile;
    }
