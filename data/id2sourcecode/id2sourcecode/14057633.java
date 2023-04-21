    public void process(String inputReaderFileName, String fileResultName) {
        File inputReaderFile = new File(inputReaderFileName);
        IExtractorInputReader reader = readerDao.read(inputReaderFile);
        ProcessReaderInfo processReaderInfo = new ProcessReaderInfo();
        processReaderInfo.setThresholdCoef(1.2);
        SampleInfo info = processReader.processReader(reader, processReaderInfo);
        readerDao.write(info.getReader(), getAppendedFile(inputReaderFile, fileResultName, "sspnt.xml"));
        markerDao.write(info.getMarkerSetHolder(), getAppendedFile(inputReaderFile, fileResultName, "mspnt.xml"));
    }
