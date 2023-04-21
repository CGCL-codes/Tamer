    public void doSaveAs() {
        FileDialog dlgSave = new FileDialog(getSite().getShell(), SWT.SAVE);
        dlgSave.setText("Save aXLang model as...");
        if (Session.getFile() != null) {
            dlgSave.setFilterPath(Session.getFile().getPath());
        }
        String[] sFilters = { "*" + RCAConstants.EXT_AXL, "*" + RCAConstants.EXT_XML };
        dlgSave.setFilterExtensions(sFilters);
        String sFileName = dlgSave.open();
        if (sFileName == null) {
            return;
        }
        File theFile = new File(sFileName);
        if (theFile.exists()) {
            MessageBox msgOverwrite = new MessageBox(getSite().getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
            msgOverwrite.setMessage("File " + sFileName + " already exists.  Overwrite?");
            msgOverwrite.setText("File exists.");
            int iResult = msgOverwrite.open();
            if (iResult != SWT.YES) {
                return;
            }
        }
        Session.setFile(theFile);
        doSave(null);
    }
