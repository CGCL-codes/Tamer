    private void train(CommandLine script) throws Exception {
        ModuleDefinition def = createModelDefinition(script);
        if (def == null) return;
        if (output != null) {
            File f = new File(output);
            if (f.exists() && !overwrite) throw new Exception("Output already exists");
        }
        EntailmentEngine engine = (EntailmentEngine) EditsModuleLoader.loadModule(def);
        System.out.println(EditsToString.toString(engine.definition()));
        List<String> files = FileTools.inputFiles(script.getArgs());
        EDITSIterator<EntailmentPair> all = inputIterator(files, useMemory);
        EvaluationStatistics stats = engine.train(all);
        if (output != null) saveModel(output, engine, stats, files, overwrite);
        System.out.println("=== Performance On Training ===\n");
        System.out.println(EngineToString.toString(stats));
    }
