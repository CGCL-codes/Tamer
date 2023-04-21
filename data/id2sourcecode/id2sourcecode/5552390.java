    public static String readCsvContent(Sheet a_sheet, String a_separator, int a_startCol, int a_startRow) throws IOException {
        StringWriter l_writer = new StringWriter();
        readCsvContent(l_writer, a_sheet, a_separator, a_startCol, a_startRow);
        return l_writer.toString();
    }
