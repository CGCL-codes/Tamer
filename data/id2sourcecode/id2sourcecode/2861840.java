    public void collectResutlsTR(ResultCollector.ResultReaderTR reader, ResultCollector.PostProcessor processor, ResultCollector.ResultWriter writer, String TRNSYSResultFile, boolean onthefly) {
        if (onthefly) {
            throw new UnsupportedOperationException("Not supported yet.");
        } else {
            ResultHeader = new HashMap<String, Integer>();
            ResultTable = new ArrayList<ArrayList<String>>();
            reader.readResultTR(JobOwner, OutputDir, ResultHeader, ResultTable, TRNSYSResultFile);
            if (processor != null) processor.postProcess(ResultHeader, ReportTable);
            writer.writeResult(ResultHeader, ResultTable);
        }
    }
