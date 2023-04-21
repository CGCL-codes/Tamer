    private void handleExport() {
        if (txt_filename.getText() == null || txt_filename.getText().trim().length() == 0) {
            MessageDialog.openError(getShell(), "Export", "An Export name has not been specified");
            return;
        }
        if (txt_dir.getText() == null || txt_dir.getText().trim().length() == 0) {
            MessageDialog.openError(getShell(), "Export", "An Export destination has not been specified");
            return;
        }
        String exportPath = txt_dir.getText() + File.separator + txt_filename.getText();
        if (!exportPath.toLowerCase().endsWith(".zip")) {
            exportPath += ".zip";
        }
        File isDa = new File(exportPath);
        if (isDa.exists()) {
            MessageBox errormessage = new MessageBox(getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
            errormessage.setText("Export Warning");
            errormessage.setMessage("The given file already exists. Do you really want to overwrite it?");
            int status = errormessage.open();
            if (status == SWT.CANCEL) return;
        }
        try {
            container.getRessourceManager().saveGLMIMS(exportPath);
            MessageDialog.openInformation(getShell(), "Export", "Export successful!");
        } catch (Exception ex) {
            MessageDialog.openError(getShell(), "Export", ex.getMessage());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
