    private void generateReport() {
        VerySimpleDateFormat dateFormat = new VerySimpleDateFormat(JMoneyPlugin.getDefault().getDateFormat());
        fromDate = fromField.getDate();
        toDate = toField.getDate();
        try {
            String reportFile = subtotalsCheckBox.getSelection() ? "resources/ItemizedIncomeExpense.jasper" : "resources/ItemizedIncomeExpense.jasper";
            URL url = ReportsPlugin.class.getResource(reportFile);
            if (url == null) {
                Object[] messageArgs = new Object[] { reportFile };
                String errorText = new java.text.MessageFormat("{0} not found.  A manual build of the net.sf.jmoney.reports project may be necessary.", java.util.Locale.US).format(messageArgs);
                JMoneyPlugin.log(new Status(IStatus.ERROR, ReportsPlugin.PLUGIN_ID, IStatus.ERROR, errorText, null));
                MessageDialog errorDialog = new MessageDialog(shell, "JMoney Build Error", null, errorText, MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
                errorDialog.open();
                return;
            }
            InputStream is = url.openStream();
            Map<String, String> params = new HashMap<String, String>();
            params.put("Title", ReportsPlugin.getResourceString("Report.IncomeExpense.Title"));
            params.put("Subtitle", dateFormat.format(fromDate) + " - " + dateFormat.format(toDate));
            params.put("Total", ReportsPlugin.getResourceString("Report.Total"));
            params.put("Category", ReportsPlugin.getResourceString("Entry.category"));
            params.put("Income", ReportsPlugin.getResourceString("Report.IncomeExpense.Income"));
            params.put("Expense", ReportsPlugin.getResourceString("Report.IncomeExpense.Expense"));
            params.put("DateToday", dateFormat.format(new Date()));
            params.put("Page", ReportsPlugin.getResourceString("Report.Page"));
            Collection items = getItems();
            if (items.isEmpty()) {
                MessageDialog dialog = new MessageDialog(shell, ReportsPlugin.getResourceString("Panel.Report.EmptyReport.Title"), null, ReportsPlugin.getResourceString("Panel.Report.EmptyReport.Message"), MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
                dialog.open();
            } else {
                JRDataSource ds = new JRBeanCollectionDataSource(items);
                JasperPrint print = JasperFillManager.fillReport(is, params, ds);
                viewer.getReportViewer().setDocument(print);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
