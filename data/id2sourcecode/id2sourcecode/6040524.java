    public void generateReport(String title) {
        try {
            System.out.println("in generate Report 27" + dtm.getRowCount());
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/NEWGEN_JR/AccessionRegister.xml");
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(url.openStream());
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            java.util.Map param = new java.util.HashMap();
            param.put("ReportTitle", title);
            param.put("MaxSalary", new Double(25000.00));
            Class.forName("org.postgresql.Driver");
            net.sf.jasperreports.engine.export.JRHtmlExporter exporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
            exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            net.sf.jasperreports.engine.JasperPrint jp = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, param, new net.sf.jasperreports.engine.data.JRTableModelDataSource(dtm));
            if (jp.getPages().size() != 0) net.sf.jasperreports.view.JasperViewer.viewReport(jp, false); else javax.swing.JOptionPane.showMessageDialog(reports.DeskTopFrame.getInstance(), "There are no records in the selected report option.");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
