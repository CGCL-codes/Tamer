    private void writeSpreadsheet() {
        System.out.println("Writing output to " + params.outfile);
        Workbook workbook = excelhelper.createWorkbook();
        excelhelper.createWorksheet(workbook, dataframe, "patients");
        excelhelper.writeWorkbook(workbook, params.outfile);
    }
