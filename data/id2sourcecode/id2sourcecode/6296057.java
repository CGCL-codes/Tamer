    private static JasperReport reportFor(String reportName) {
        try {
            return JasperCompileManager.compileReport(urlFor(reportName).openStream());
        } catch (Exception e) {
            throw new ReportPrintException(e);
        }
    }
