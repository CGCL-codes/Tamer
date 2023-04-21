    public void test_1() throws Throwable {
        File l_fileTemp = FTools.getTempFile("test-excel.xls", false);
        l_fileTemp.delete();
        WritableWorkbook l_workbook = Workbook.createWorkbook(l_fileTemp);
        WritableSheet l_sheet = l_workbook.createSheet("test", 100);
        l_sheet.addCell(new Label(2, 3, "Valeur 1"));
        l_sheet.addCell(new Number(2, 4, 100));
        l_workbook.createSheet("test2", 100);
        l_sheet = l_workbook.createSheet("csv", 100);
        String l_csv = GB_RandomTools.randomCvsContentStr(10, 20, "\t", true);
        GB_ExcelTools.writeCvsContent(l_sheet, l_csv, "\t", 2, 5);
        String l_csv2 = GB_ExcelTools.readCsvContent(l_sheet, "\t", 2, 5);
        l_csv = GB_StringTools.trimEndLines(l_csv);
        l_csv2 = GB_StringTools.trimEndLines(l_csv2);
        assertEquals("read-write", l_csv, l_csv2);
        l_workbook.write();
        l_workbook.close();
    }
