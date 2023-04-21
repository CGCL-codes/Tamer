    public void generateCompleteSerieswiseAccessionReport(String fileLocation, String formatType, String title) {
        try {
            System.out.println("in generate Report 27" + dtm.getRowCount());
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/NEWGEN_JR/AccessionRegister.xml");
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(url.openStream());
            System.out.println("in generate Report 30" + dtm.getRowCount());
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            System.out.println("in generate Report 32" + dtm.getRowCount());
            java.util.Map param = new java.util.HashMap();
            param.put("ReportTitle", title);
            param.put("MaxSalary", new Double(25000.00));
            Class.forName("org.postgresql.Driver");
            System.out.println("in generate Report 37" + dtm.getRowCount());
            net.sf.jasperreports.engine.JasperPrint jp = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, param, new net.sf.jasperreports.engine.data.JRTableModelDataSource(dtm));
            System.out.println("in generate Report 39" + dtm.getRowCount());
            if (formatType.equals("xls")) {
                net.sf.jasperreports.engine.export.JRXlsExporter exporterXLS = new net.sf.jasperreports.engine.export.JRXlsExporter();
                exporterXLS.setParameter(net.sf.jasperreports.engine.export.JRXlsExporterParameter.JASPER_PRINT, jp);
                exporterXLS.setParameter(net.sf.jasperreports.engine.export.JRXlsExporterParameter.OUTPUT_FILE_NAME, fileLocation);
                exporterXLS.setParameter(net.sf.jasperreports.engine.export.JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporterXLS.setParameter(net.sf.jasperreports.engine.export.JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(net.sf.jasperreports.engine.export.JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.exportReport();
            }
            if (formatType.equals("pdf")) {
                net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jp, fileLocation);
            }
            System.out.println("in generate Report 43" + dtm.getRowCount());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
