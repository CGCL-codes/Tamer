    @SuppressWarnings("unchecked")
    public static void mainEX(String[] args) throws Exception {
        CommandExecutor exex = new CommandExecutor();
        new Edits(exex);
        Options oxsx = exex.defaultOptions();
        Options oxs = new Options();
        for (Object x : oxsx.getOptions()) {
            Option xx = (Option) x;
            if (xx.getLongOpt().equals("configuration")) continue;
            if (xx.getLongOpt().equals("model")) continue;
            if (xx.getLongOpt().equals("output")) {
                xx.setDescription("Extracted idfs");
                xx.setRequired(true);
            }
            oxs.addOption(xx);
        }
        Option o = new Option("mi", "make_index", false, "Make an idf index");
        oxs.addOption(o);
        if (args.length == 0) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            HelpFormatter formatter = new HelpFormatter();
            printWriter.println("IDF Calculator Edits Script");
            printWriter.println("EDITS - Edit Distance Textual Entailment Suite - " + Edits.VERSION);
            formatter.printUsage(printWriter, HelpFormatter.DEFAULT_WIDTH, "edits-idf");
            formatter.printOptions(printWriter, 120, oxs, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD);
            System.out.println(result.toString());
            return;
        }
        CommandLine line = null;
        try {
            line = new BasicParser().parse(oxs, args);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            return;
        }
        Edits.setVerbose(line.hasOption("verbose"));
        String output = line.getOptionValue("output");
        boolean overwrite = line.hasOption("force");
        if (output != null && !overwrite) throw new Exception("Output already exists");
        List<String> files = FileTools.inputFiles(line.getArgList());
        EDITSIterator<EntailmentPair> all = null;
        if (!line.hasOption("pipe")) all = FilesEPSource.loadFromShell(files); else all = FilesEPSource.initFromShell(files);
        calculateIDF(all, line.getOptionValue("output"), line.hasOption("make_index"));
    }
