    public static void mainEX(String[] args) throws Exception {
        CommandExecutor exec = new CommandExecutor();
        Options xosx = exec.defaultOptions();
        Options oxs = new Options();
        for (Object x : xosx.getOptions()) {
            Option xx = (Option) x;
            if (xx.getLongOpt().equals("configuration")) {
                continue;
            }
            if (xx.getLongOpt().equals("model")) {
                xx.setDescription("Tree Tagger path");
                xx.setRequired(true);
            }
            if (xx.getLongOpt().equals("output")) {
                xx.setDescription("Annotated dataset path");
                xx.setRequired(true);
            }
            oxs.addOption(xx);
        }
        if (args.length == 0) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            HelpFormatter formatter = new HelpFormatter();
            printWriter.println("Tree Taggger Edits Script");
            printWriter.println("EDITS - Edit Distance Textual Entailment Suite - " + Edits.VERSION);
            formatter.printUsage(printWriter, HelpFormatter.DEFAULT_WIDTH, "edits-treetagger");
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
        new Edits(exec);
        String output = line.getOptionValue("output");
        boolean overwrite = line.hasOption("force");
        if (output != null && !overwrite && new File(output).exists()) throw new Exception("Output already exists");
        Edits.setVerbose(line.hasOption("verbose"));
        String path = line.getOptionValue("model");
        TextAnnotator annotator = new TreeTagger(path);
        EDITSIterator<EntailmentPair> source = null;
        Target<EntailmentPair> target = new FileEPTarget(line.getOptionValue("output"), true);
        if (!line.hasOption("pipe")) source = new EDITSListIterator<EntailmentPair>(((EntailmentCorpus) new ObjectFactory().load(line.getArgs()[0])).getPair()); else source = new EntailmentPairSource(line.getArgs()[0]);
        annotator.annotate(source, target);
        target.close();
    }
