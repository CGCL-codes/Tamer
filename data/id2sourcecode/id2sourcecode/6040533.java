    public void generatePatronReport(String title) {
        try {
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/NEWGEN_JR/PatronReport.xml");
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(url.openStream());
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            java.util.Map param = new java.util.HashMap();
            param.put("ReportTitle", title);
            param.put("MaxSalary", new Double(25000.00));
            Class.forName("org.postgresql.Driver");
            net.sf.jasperreports.engine.JasperPrint jp = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, param, new net.sf.jasperreports.engine.data.JRTableModelDataSource(dtm));
            dtm.setRowCount(0);
            if (jp.getPages().size() != 0) net.sf.jasperreports.view.JasperViewer.viewReport(jp, false); else javax.swing.JOptionPane.showMessageDialog(reports.DeskTopFrame.getInstance(), "There are no records in the selected report option.");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
