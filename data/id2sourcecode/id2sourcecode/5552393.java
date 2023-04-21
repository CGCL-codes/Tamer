    public static void readCsvLine(Writer a_writer, Sheet a_sheet, String a_separator, int a_startCol, int a_row) throws IOException {
        Cell[] l_cols = a_sheet.getRow(a_row);
        int len = l_cols.length;
        for (int i = a_startCol; i < len; i++) {
            Cell l_col = l_cols[i];
            String l_value = l_col.getContents();
            l_value = GB_CsvTools.encodeSL(l_value);
            if (i != (len - 1)) {
                l_value += a_separator;
            }
            a_writer.write(l_value);
        }
        a_writer.write(AA.SL);
    }
