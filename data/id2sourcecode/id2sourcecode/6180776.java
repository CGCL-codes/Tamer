    private boolean writeCsv(IExtractorInputReader reader, File selectedFile) {
        WorkServiceFactory.createCsvDao().write(reader, selectedFile);
        return false;
    }
