    public void act() throws FileNotFoundException, IOException, FileFormatException, AbnormalTerminationException {
        EnumSet<Arguments> args = EnumSet.of(Arguments.OPDF, Arguments.NB_STATES, Arguments.OUT_HMM, Arguments.IN_SEQ);
        CommandLineArguments.checkArgs(args);
        int nbStates = Arguments.NB_STATES.getAsInt();
        OutputStream outStream = Arguments.OUT_HMM.getAsOutputStream();
        Writer writer = new OutputStreamWriter(outStream);
        InputStream st = Arguments.IN_SEQ.getAsInputStream();
        Reader reader = new InputStreamReader(st);
        learn(nbStates, Types.relatedObjs(), reader, writer);
        writer.flush();
    }
