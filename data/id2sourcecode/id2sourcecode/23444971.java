    public ExcelFile(File readFile, File writeFile) throws IOException {
        this.mode = Mode.Update;
        Workbook workbook = getWorkbook(readFile, null);
        writableWorkbook = Workbook.createWorkbook(writeFile, workbook);
        workbook.close();
        sheet = writableWorkbook.getSheet(0);
    }
