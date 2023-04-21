    public void export(String filename, String date) throws IOException {
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(date);
        writeHeader(wb, sheet, "ID", "Level", "Datum", "Thread", "User", "Modul", "Klasse", "Meldung", "Trace");
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 2000);
        sheet.setColumnWidth(2, 3700);
        sheet.setColumnWidth(3, 2000);
        sheet.setColumnWidth(4, 2000);
        sheet.setColumnWidth(5, 2000);
        sheet.setColumnWidth(6, 12000);
        sheet.setColumnWidth(7, 15000);
        sheet.setColumnWidth(8, 10000);
        for (int i = 1; i <= rows.size(); i++) {
            Row row = sheet.createRow(i);
            LogEventRow logEvent = rows.get(i - 1);
            row.createCell(0).setCellValue(logEvent.getId());
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yy hh:mm:ss"));
            Cell cell = row.createCell(1);
            cell.setCellValue(logEvent.getLevelEnum().toString());
            cell = row.createCell(2);
            cell.setCellValue(new Date(logEvent.getDatum()));
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellValue(trim32000(logEvent.getThreadName()));
            cell = row.createCell(4);
            cell.setCellValue(trim32000(logEvent.getUserid()));
            cell = row.createCell(5);
            cell.setCellValue(trim32000(logEvent.getModul()));
            cell = row.createCell(6);
            cell.setCellValue(trim32000(logEvent.getKlasse()));
            cell = row.createCell(7);
            cell.setCellValue(trim32000(logEvent.getMessage()));
            cell = row.createCell(8);
            cell.setCellValue(trim32000(logEvent.getTraceShort()));
        }
        FileOutputStream fileOut = new FileOutputStream(filename);
        wb.write(fileOut);
        fileOut.close();
    }
