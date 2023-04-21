    static ObjectStream<POSSample> openSampleData(String sampleDataName, File sampleDataFile, Charset encoding) {
        CmdLineUtil.checkInputFile(sampleDataName + " Data", sampleDataFile);
        FileInputStream sampleDataIn = CmdLineUtil.openInFile(sampleDataFile);
        ObjectStream<String> lineStream = new PlainTextByLineStream(sampleDataIn.getChannel(), encoding);
        return new WordTagSampleStream(lineStream);
    }
