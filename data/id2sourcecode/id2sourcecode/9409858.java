    private ExcelFile(String fileName, String mainSheetName, boolean append, boolean inLog) throws Exception {
        sheets[0] = mainSheetName;
        if (!fileName.toLowerCase().endsWith(".xls")) {
            fileName = fileName + ".xls";
        }
        File fileFile = new File(testName() + "\\" + fileName);
        if (fileFile.length() > 0) {
            FileUtils.copyFile(testName() + "\\" + fileName, fileName + ".backup");
        }
        this.fileName = fileName;
        this.append = append;
        init(inLog);
    }
