    private void writeStory(HSSFSheet sheet, SpreadsheetStory spreadsheetStory, int i) {
        HSSFRow row = sheet.createRow(i);
        setCellValue(row, STORY_END_DATE_COL, spreadsheetStory.getEndDate());
        setCellValue(row, TITLE_COL, spreadsheetStory.getTitle());
        setCellValue(row, STATUS_COL, spreadsheetStory.getStatus());
        setCellValue(row, STORY_PRIORITY_COL, spreadsheetStory.getPriority());
        setCellValue(row, ESTIMATE_NUMBER_COL, spreadsheetStory.getEstimate());
    }
