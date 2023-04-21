    @Override
    public String dumpPipeline() {
        return sink.dumpPipeline() + "\n" + getClass().getName() + ": " + reader + "->" + writer + "->" + readerForFaucet;
    }
