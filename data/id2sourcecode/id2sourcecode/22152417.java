    @Override
    public String dumpPipeline() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (ISink sink : sinks) {
            sb.append(sink.dumpPipeline() + "\n" + getClass().getName() + ": " + reader + "->" + writers.get(index) + "->" + readerForFaucets.get(index));
            index++;
        }
        return sb.toString();
    }
