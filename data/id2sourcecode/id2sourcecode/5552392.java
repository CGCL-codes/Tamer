    public static void readCsvContent(Writer a_writer, Sheet a_sheet, String a_separator, int a_startCol, int a_startRow) throws IOException {
        int len = a_sheet.getRows();
        for (int i = a_startRow; i < len; i++) {
            readCsvLine(a_writer, a_sheet, a_separator, a_startCol, i);
        }
    }
