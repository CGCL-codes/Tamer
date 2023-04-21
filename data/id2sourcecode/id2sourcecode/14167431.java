    private void appendReport(StringBuilder finalReport, DmaThreads dmaThreads, boolean writeFullReportUnconditionally) {
        if (dmaThreads != null) {
            if (writeFullReportUnconditionally || dmaArgs.allReport()) {
                finalReport.append(dmaThreads.getFullReport());
            } else {
                finalReport.append(dmaThreads.getTotalReport());
            }
        }
    }
