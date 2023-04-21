    protected boolean writeSample(IExtractorInputReader reader, File file) {
        WorkServiceFactory.createReaderDao().write(reader, file);
        return true;
    }
