    protected String generate(String inScriptName) {
        InMemoryScriptWriter writer = new InMemoryScriptWriter();
        ScriptReader reader = createScriptReader();
        Processor processor = new Processor(reader, writer);
        processor.process(inScriptName);
        return writer.getScript();
    }
