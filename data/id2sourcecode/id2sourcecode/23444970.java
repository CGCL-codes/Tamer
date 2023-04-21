    public ExcelFile(String readFilename, String writeFilename) throws IOException {
        this(new File(readFilename), new File(writeFilename));
    }
