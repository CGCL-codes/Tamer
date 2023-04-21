    @Test
    @Ignore
    public void testConvertSpreadsheet() {
        String filename = "c:/projects/seqtagutils/data/filemaker/fmhcv.xlsx";
        CExcelHelper excelhelper = new CExcelHelper();
        CTable table = excelhelper.extractTable(filename);
        for (CTable.Row row : table.getRows()) {
            System.out.println(row.getValue(0));
        }
        CFileHelper.writeFile("c:/temp/spreadsheet.txt", table.toString());
    }
