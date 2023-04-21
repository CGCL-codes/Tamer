    @Override
    public void write(final SpreadsheetDocument document, final OutputStream outputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (Sheet sheet : document.getSheets()) {
            this.handleSheet(workbook, sheet);
        }
        workbook.write(outputStream);
    }
