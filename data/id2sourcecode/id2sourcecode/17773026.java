    private void test(CommandLine script) throws Exception {
        List<String> files = FileTools.inputFiles(script.getArgs());
        String model = script.getOptionValue("model");
        EntailmentEngine engine = null;
        if (model == null) {
            System.out.println("Initializing engine with default values.");
            ModuleDefinition def = createModelDefinition(script);
            if (def == null) return;
            if (output != null) {
                File f = new File(output);
                if (f.exists() && !overwrite) throw new Exception("Output already exists");
            }
            engine = (EntailmentEngine) EditsModuleLoader.loadModule(def);
        } else {
            System.out.println("Loading Model " + model);
            EntailmentEngineModel engineModel = (EntailmentEngineModel) EDITSModel.loadModel(model, Edits.tempdir());
            engine = engineModel.engine();
        }
        MultiThreadEngine mte = new MultiThreadEngine(engine);
        Target<EvaluationResult> result = createTarget();
        EvaluationStatistics stats = new EvaluationStatistics();
        for (String file : files) {
            EDITSIterator<EntailmentPair> source = inputIterator(file);
            EvaluationStatistics s = mte.test(source, result);
            if (s != null) {
                System.out.println("=== Test Result ===\n");
                if (files.size() > 1) System.out.println("File: " + file + "\n");
                System.out.println(s.toString());
                stats.add(s);
            }
        }
        if (result != null) result.close();
        if (files.size() > 1) {
            stats.init();
            System.out.println("=== Test Result ===\n");
            System.out.println(stats);
        }
    }
