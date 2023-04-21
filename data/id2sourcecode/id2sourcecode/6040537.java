    public void generateCheckOutList(String fileLocation1, String reportTitle) {
        try {
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/NEWGEN_JR/" + fileLocation1);
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(url.openStream());
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            java.util.Map param = new java.util.HashMap();
            param.put("ReportTitle", reportTitle);
            Class.forName("org.postgresql.Driver");
            net.sf.jasperreports.engine.JasperPrint jp = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, param, new net.sf.jasperreports.engine.data.JRTableModelDataSource(dtm));
            java.sql.Timestamp currentTime = new java.sql.Timestamp(java.util.Calendar.getInstance().getTimeInMillis());
            if (jp.getPages().size() != 0) net.sf.jasperreports.view.JasperViewer.viewReport(jp, false); else javax.swing.JOptionPane.showMessageDialog(reports.DeskTopFrame.getInstance(), "There are no records in the selected report option.");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
