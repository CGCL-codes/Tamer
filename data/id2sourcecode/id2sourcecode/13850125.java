    @Override
    public void write(final SpreadsheetDocument document, final OutputStream outputStream) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        for (Sheet sheet : document.getSheets()) {
            this.handleSheet(workbook, sheet);
        }
        workbook.write(outputStream);
    }
