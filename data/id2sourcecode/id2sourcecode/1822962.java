    @SuppressWarnings("unchecked")
    public static JasperPrint compileReport(String mainReportFileNameWithoutExtension, String subReportFileNameWithoutExtension, JRDataSource mainDataSource, JRDataSource subReportDataSource) throws JRException, IOException {
        File tempSubreportsDirectory = new File(System.getProperty("java.io.tmpdir"), "subreports");
        if (!tempSubreportsDirectory.exists()) {
            tempSubreportsDirectory.mkdir();
        }
        File mainFileReport = File.createTempFile(JASPER_REPORT, JRXML_EXTENSION);
        File subFileReport = File.createTempFile(JASPER_REPORT, JRXML_EXTENSION, tempSubreportsDirectory);
        FileUtils.copyFile(new File(mainReportFileNameWithoutExtension + JRXML_EXTENSION), mainFileReport);
        FileUtils.copyFile(new File(subReportFileNameWithoutExtension + JRXML_EXTENSION), subFileReport);
        File tempReportFile = File.createTempFile("jasperreport", ".jasper");
        String prefix = subReportFileNameWithoutExtension.substring(subReportFileNameWithoutExtension.lastIndexOf('/') + 1);
        File tempSubReportFile = new File(tempSubreportsDirectory, prefix + ".jasper");
        JasperCompileManager.compileReportToFile(mainFileReport.getAbsolutePath(), tempReportFile.getAbsolutePath());
        JasperCompileManager.compileReportToFile(subFileReport.getAbsolutePath(), tempSubReportFile.getAbsolutePath());
        @SuppressWarnings("rawtypes") Map parameters = new HashMap();
        parameters.put("EXTERNAL_DATA", mainDataSource);
        JasperPrint result = JasperFillManager.fillReport(tempReportFile.getAbsolutePath(), parameters, subReportDataSource);
        try {
            mainFileReport.delete();
            subFileReport.delete();
            tempReportFile.delete();
            tempSubReportFile.delete();
            tempSubreportsDirectory.delete();
        } catch (Exception e) {
            LOGGER.error("Temp files could not be deleted.", e);
        }
        return result;
    }
