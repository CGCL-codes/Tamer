    public void writeStories(List stories) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Features");
        writeHeader(sheet);
        for (int i = 0; i < stories.size(); i++) {
            SpreadsheetStory spreadsheetStory = (SpreadsheetStory) stories.get(i);
            writeStory(sheet, spreadsheetStory, i + 1);
        }
        wb.write(output);
        output.close();
    }
