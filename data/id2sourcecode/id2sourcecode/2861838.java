    public void collectReports(ResultCollector.ReportReader reader, ResultCollector.ReportWriter writer, boolean onthefly) {
        if (onthefly) {
            throw new UnsupportedOperationException("Not supported yet.");
        } else {
            ReportHeader = new ArrayList<String>();
            ReportTable = new ArrayList<ArrayList<String>>();
            reader.readReport(JobOwner, OutputDir, ReportHeader, ReportTable);
            writer.writeReport(ReportHeader, ReportTable);
        }
    }
