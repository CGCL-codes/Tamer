    private boolean writeArff(IExtractorInputReader reader, File selectedFile) {
        WorkServiceFactory.createWekaArffDao().write(reader, selectedFile);
        return false;
    }
