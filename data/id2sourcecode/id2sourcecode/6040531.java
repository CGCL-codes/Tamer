    public void generateCurrentReservationReport() {
        try {
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/NEWGEN_JR/CurrentReservationReport.xml");
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(url.openStream());
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            java.util.Map param = new java.util.HashMap();
            String UTC = "";
            java.util.Calendar cl = java.util.Calendar.getInstance();
            String month = String.valueOf((cl.get(java.util.Calendar.MONTH) + 1));
            if (month.trim().length() == 1) month = "0" + month.trim();
            String day = String.valueOf(cl.get(java.util.Calendar.DATE));
            if (day.trim().length() == 1) day = "0" + day.trim();
            UTC = cl.get(java.util.Calendar.YEAR) + "-" + month + "-" + day;
            param.put("ReportTitle", "Report of Reservations for items as on '" + UTC + "'");
            param.put("MaxSalary", new Double(25000.00));
            Class.forName("org.postgresql.Driver");
            net.sf.jasperreports.engine.JasperPrint jp = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, param, new net.sf.jasperreports.engine.data.JRTableModelDataSource(dtm));
            dtm.setRowCount(0);
            if (jp.getPages().size() != 0) net.sf.jasperreports.view.JasperViewer.viewReport(jp, false); else javax.swing.JOptionPane.showMessageDialog(reports.DeskTopFrame.getInstance(), "There are no records in the selected report option.");
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
