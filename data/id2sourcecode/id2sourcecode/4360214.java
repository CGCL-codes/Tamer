    private static CsvWriter initializeFile(File file) throws IOException {
        file.createNewFile();
        CsvWriter csvWriter = new CsvWriter(file);
        csvWriter.writeLine("Date", "Email Address", "Display Name", "Title", "Description", "Thread Number");
        return csvWriter;
    }
