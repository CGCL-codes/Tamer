    @Override
    public String dumpPipeline() {
        return sink.dumpPipeline() + "\n" + getClass().getName() + ": " + writer + "->" + readerForFaucet;
    }
