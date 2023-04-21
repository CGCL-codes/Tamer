    public void collectResutls(ResultCollector.ResultReader reader, ResultCollector.PostProcessor processor, ResultCollector.ResultWriter writer, boolean onthefly) {
        if (onthefly) {
            throw new UnsupportedOperationException("Not supported yet.");
        } else {
            ResultHeader = new HashMap<String, Integer>();
            ResultTable = new ArrayList<ArrayList<String>>();
            reader.readResult(JobOwner, OutputDir, ResultHeader, ResultTable);
            if (processor != null) processor.postProcess(ResultHeader, ReportTable);
            writer.writeResult(ResultHeader, ResultTable);
        }
    }
