    @Override
    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        ((CopyDataWizardPanel3) panels[2]).reload();
        ((CopyDataWizardPanel1) panels[0]).reload();
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Data Export Wizard");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            try {
                TableNode table = ((CopyDataWizardPanel1) panels[0]).tableSelected();
                Connection conn = ((CopyDataWizardPanel3) panels[2]).getConnection();
                DBAnalizer analizer = new DBAnalizer(table.getMetaData().getConn());
                String filter = ((CopyDataWizardPanel2) panels[1]).getFilter();
                IOHelper.writeInfo("Begin reading data");
                analizer.readFilteredRelatedData(table.getMetaData(), null, null, filter, null, false);
                IOHelper.writeInfo("Finished reading data");
                IOHelper.writeInfo("Begin writing data");
                analizer.writeAllRelatedData(conn, table.getMetaData(), null, null);
                IOHelper.writeInfo("Finished writing data");
            } catch (Exception ex) {
                IOHelper.writeError(ex.getMessage(), ex);
            }
        }
    }
