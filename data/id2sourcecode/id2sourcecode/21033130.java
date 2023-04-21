    private void export() {
        int[] pcExports;
        final String templateName = (String) templateList.getSelectedValue();
        final int idx = templateName.lastIndexOf('.');
        String extension = "";
        if (idx >= 0) {
            extension = templateName.substring(idx + 1);
        }
        final PFileChooser fcExport = new PFileChooser();
        fcExport.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fcExport.setCurrentDirectory(new File(SettingsHandler.getHTMLOutputSheetPath()));
        fcExport.addChoosableFileFilter(null, "All Files (*.*)");
        String desc;
        if ("htm".equalsIgnoreCase(extension)) {
            desc = "HTML Documents";
        } else if ("xml".equalsIgnoreCase(extension)) {
            desc = "XML Documents";
        } else {
            desc = extension + " Files";
        }
        fcExport.addChoosableFileFilter(extension, desc + " (*." + extension + ")");
        if (!partyMode) {
            pcExports = pcList.getSelectedIndices();
        } else {
            pcExports = new int[] { -2 };
        }
        for (int loop = 0; loop < pcExports.length; loop++) {
            final String pcName = partyMode ? "Entire Party" : (String) pcList.getModel().getElementAt(pcExports[loop]);
            fcExport.setSelectedFile(new File(SettingsHandler.getPcgPath().toString() + File.separator + pcName + "." + extension));
            fcExport.setDialogTitle("Export " + pcName);
            if (fcExport.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                continue;
            }
            final String aFileName = fcExport.getSelectedFile().getAbsolutePath();
            if (aFileName.length() < 1) {
                ShowMessageDelegate.showMessageDialog("You must set a filename.", "PCGen", MessageType.ERROR);
                continue;
            }
            try {
                final File outFile = new File(aFileName);
                if (outFile.isDirectory()) {
                    ShowMessageDelegate.showMessageDialog("You cannot overwrite a directory with a file.", "PCGen", MessageType.ERROR);
                    continue;
                }
                if (outFile.exists() && SettingsHandler.getAlwaysOverwrite() == false) {
                    int reallyClose = JOptionPane.showConfirmDialog(this, "The file " + outFile.getName() + " already exists, are you sure you want to overwrite it?", "Confirm overwriting " + outFile.getName(), JOptionPane.YES_NO_OPTION);
                    if (reallyClose != JOptionPane.YES_OPTION) {
                        continue;
                    }
                }
                printToFile(outFile, pcExports[loop]);
                Globals.executePostExportCommandStandard(aFileName);
            } catch (IOException ex) {
                ShowMessageDelegate.showMessageDialog("Could not export " + pcName + ". Try another filename.", "PCGen", MessageType.ERROR);
                Logging.errorPrint("Could not export " + pcName, ex);
            }
        }
    }
