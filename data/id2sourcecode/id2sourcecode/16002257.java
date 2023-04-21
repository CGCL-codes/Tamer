    @Test
    public void testSerialiseErlang() throws IOException {
        LearnerGraph grOrig = buildLearnerGraph("A- {call, read} ->B<-{call, read}-C-{call, {write,aa}}->A", "testConstructErlGraph1", config);
        File file = new File("ErlangExamples/locker/locker.erl");
        ErlangModule mod = ErlangModule.loadModule(ErlangModule.setupErlangConfiguration(file));
        LearnerGraph erlangGraph = grOrig.transform.interpretLabelsOnGraph(mod.behaviour.new ConverterErlToMod());
        StringWriter writer = new StringWriter();
        erlangGraph.transform.interpretLabelsOnGraph(mod.behaviour.new ConverterModToErl()).storage.writeGraphML(writer);
        LearnerGraph loaded = new LearnerGraph(config.copy());
        AbstractPersistence.loadGraph(new StringReader(writer.toString()), loaded);
        LearnerGraph erlangLoadedGraph = loaded.transform.interpretLabelsOnGraph(mod.behaviour.new ConverterErlToMod());
        Assert.assertNull(WMethod.checkM(erlangGraph, erlangGraph.getInit(), erlangLoadedGraph, erlangLoadedGraph.getInit(), VERTEX_COMPARISON_KIND.DEEP));
    }
