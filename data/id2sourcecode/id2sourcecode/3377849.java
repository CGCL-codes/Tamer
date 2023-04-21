    @Override
    public void write(IExtractorInputReader reader, OutputStream outputStream) {
        try {
            saveDataToFile(reader, outputStream);
        } catch (Exception e) {
            LOG.error(e);
        }
    }
