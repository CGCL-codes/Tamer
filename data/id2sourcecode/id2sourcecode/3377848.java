    @Override
    public void write(IExtractorInputReader reader, File file) {
        try {
            FileOutputStream outputFile = new FileOutputStream(file, false);
            saveDataToFile(reader, outputFile);
        } catch (Exception e) {
            LOG.error(e);
        }
    }
