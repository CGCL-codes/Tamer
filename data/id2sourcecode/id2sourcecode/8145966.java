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
                xx.setRequired(true);
            }
            oxs.addOption(xx);
        }
        Option o = new Option("x", "exhaustive", false, "Exhaustive search");
        o.setRequired(false);
        oxs.addOption(o);
        o = new Option("i", "iterations", true, "Genetic algorithm iterations");
        o.setRequired(false);
        oxs.addOption(o);
        o = new Option("a", "algorithm", true, "Test algorithm");
        o.setRequired(false);
        oxs.addOption(o);
        o = new Option("p", "optimize", true, "Optimize on (a py ry fy pn rn fn)");
        o.setRequired(false);
        oxs.addOption(o);
        o = new Option("t", "testset", true, "Test Set");
        oxs.addOption(o);
        if (args.length == 0) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            HelpFormatter formatter = new HelpFormatter();
            printWriter.println("Edits GA Script");
            printWriter.println("EDITS - Edit Distance Textual Entailment Suite - " + Edits.VERSION);
            formatter.printUsage(printWriter, HelpFormatter.DEFAULT_WIDTH, "edits-ga");
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
        if (output != null && new File(output).exists() && !overwrite) throw new Exception("Output already exists");
        String algorithm = line.hasOption("algorithm") ? line.getOptionValue("algorithm") : OverlapDistance.NAME;
        boolean fast = !line.hasOption("exhaustive");
        if (!fast) algorithm = null;
        int iterations = line.hasOption("iterations") ? Integer.parseInt(line.getOptionValue("iterations")) : 20;
        List<String> files = FileTools.inputFiles(line.getArgs());
        EDITSIterator<EntailmentPair> trainingS = CommandExecutor.inputIterator(files, true);
        String testFile = line.getOptionValue("testset");
        EDITSListIterator<EntailmentPair> testS = null;
        ObjectFactory f = new ObjectFactory();
        if (testFile != null) testS = new EDITSListIterator<EntailmentPair>(((EntailmentCorpus) f.load(testFile)).getPair());
        List<String> algorithms = new ArrayList<String>();
        if (!fast) {
            algorithms.add(TokenEditDistance.NAME);
            algorithms.add(CosineSimilarity.NAME);
            algorithms.add(JaroWinkler.NAME);
            algorithms.add(RougeS.NAME);
            algorithms.add(RougeW.NAME);
            algorithms.add(OverlapDistance.NAME);
        } else algorithms.add(algorithm);
        List<GeneticResult> results = new ArrayList<GeneticResult>();
        GeneticResult best = null;
        String optimize = line.hasOption("optimize") ? line.getOptionValue("optimize") : null;
        GeneticSearcher searcher = new GeneticSearcher(trainingS, testS, fast, optimize, iterations);
        for (String alg : algorithms) {
            GeneticResult result = searcher.score(alg);
            results.add(result);
            if (best == null || result.getTraining() > best.getTraining()) best = result;
        }
        EntailmentEngine e = (EntailmentEngine) EditsModuleLoader.loadModule(best.getDef());
        trainingS.reset();
        if (testS != null) testS.reset();
        EvaluationStatistics stats = e.train(trainingS);
        if (output != null) CommandExecutor.saveModel(output, e, stats, files, overwrite);
        double d = stats.value(e.trainingOptions());
        double dx = 0;
        if (testS != null) {
            MultiThreadEngine mte = new MultiThreadEngine(e);
            stats = mte.test(testS);
            dx = stats.value(e.trainingOptions());
        }
        System.out.println("######### Overal #########");
        System.out.println("Training: " + d + (testFile == null ? "" : " Test: " + dx));
    }
