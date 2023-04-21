    static ObjectStream<Parse> openTrainingData(File trainingDataFile, Charset encoding) {
        CmdLineUtil.checkInputFile("Training data", trainingDataFile);
        System.err.print("Opening training data ... ");
        FileInputStream trainingDataIn;
        try {
            trainingDataIn = new FileInputStream(trainingDataFile);
        } catch (FileNotFoundException e) {
            System.err.println("failed");
            System.err.println("File not found: " + e.getMessage());
            throw new TerminateToolException(-1);
        }
        System.err.println("done");
        return new ParseSampleStream(new PlainTextByLineStream(trainingDataIn.getChannel(), encoding));
    }
