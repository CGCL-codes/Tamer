    public void writeThreadsReport(String reportName, PrintWriter writer) {
        if (this.controller == null || this.controller.getFrontier() == null) {
            writer.println("Crawler not running.");
            return;
        }
        this.controller.getToePool().reportTo(reportName, writer);
    }
