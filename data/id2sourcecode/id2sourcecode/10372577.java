    public void testGetSheetContent() throws Exception {
        String[][] write = new String[][] { { "00", "01", "02" }, { "10" }, {}, { "30", "31", "32", "33", "34", "35", "36", "37", "38", "39" }, { "40", "41" }, { "50", "51", "52", "53", "54", "55", "56", "57", "58" }, { "60", "61", "62", "63", "64", "65", "66" }, {}, { "80", "81", "82", "83", "84", "85", "86", "87", "88", "89" } };
        setExcelFileContent(write, null);
        sleep(5000);
        ExcelFile excelFile = ExcelFile.getInstance(fileName, true);
        int num = excelFile.getNumOfRows();
        report.report("Write Data Has " + write.length + " Rows, Actual Rows Found  : " + num, (num == write.length));
        for (int i = 0; i < write.length; i++) {
            num = excelFile.getNumOfCells(i);
            report.report("Write row no." + i + " Has " + write[i].length + " Elements, Elements Found  : " + num, (num == write[i].length));
        }
        String[][] read = excelFile.getSheetContent();
        for (int i = 0; i < write.length; i++) {
            if (write[i] != null) {
                StringBuffer sbWrite = new StringBuffer();
                StringBuffer sbRead = new StringBuffer();
                for (int j = 0; j < write[i].length; j++) {
                    sbWrite.append(write[i][j] + ";");
                    sbRead.append(read[i][j] + ";");
                }
                report.report("write : " + sbWrite.toString() + ", read: " + sbRead.toString(), (sbWrite.toString().compareTo(sbRead.toString()) == 0));
            } else if (read[i] != null) {
                report.report("Write row no." + i + " is null but read row no." + i + " is not null", false);
            }
        }
        if (write.length != read.length) {
            report.report("read and write data are not equal", false);
        }
    }
