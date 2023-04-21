    private void export() {
        int[] pcExports;
        final String extension = ".pdf";
        PFileChooser fcExport = new PFileChooser();
        fcExport.setCurrentDirectory(new File(SettingsHandler.getPcgPath().toString()));
        fcExport.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fcExport.addChoosableFileFilter(null, "All Files (*.*)");
        fcExport.addChoosableFileFilter("pdf", "PDF Documents (*.pdf)");
        if (!partyMode) {
            pcExports = pcList.getSelectedIndices();
        } else {
            pcExports = new int[] { -2 };
        }
        for (int loop = 0; loop < pcExports.length; loop++) {
            final String pcName = partyMode ? "Entire Party" : (String) pcList.getModel().getElementAt(pcExports[loop]);
            fcExport.setSelectedFile(new File(SettingsHandler.getPcgPath().toString() + File.separator + pcName + extension));
            fcExport.setDialogTitle("Export " + pcName);
            try {
                if (fcExport.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                    continue;
                }
            } catch (Exception ex) {
                Logging.errorPrint("Could not show Save Dialog for " + pcName);
                continue;
            }
            final String characterFileName = fcExport.getSelectedFile().getAbsolutePath();
            if (characterFileName.length() < 1) {
                ShowMessageDelegate.showMessageDialog("You must set a filename.", "PCGen", MessageType.ERROR);
                continue;
            }
            try {
                final File outFile = new File(characterFileName);
                if (outFile.isDirectory()) {
                    ShowMessageDelegate.showMessageDialog("You cannot overwrite a directory with a file.", "PCGen", MessageType.ERROR);
                    continue;
                }
                if (outFile.exists() && SettingsHandler.getAlwaysOverwrite() == false) {
                    int reallyClose = JOptionPane.showConfirmDialog(this, "The file " + outFile.getName() + " already exists, " + "are you sure you want " + "to overwrite it?", "Confirm overwriting " + outFile.getName(), JOptionPane.YES_NO_OPTION);
                    if (reallyClose != JOptionPane.YES_OPTION) {
                        continue;
                    }
                }
                block();
                File tmpFile;
                String selectedTemplate = (String) templateList.getSelectedValue();
                if (selectedTemplate.endsWith(".xslt") || selectedTemplate.endsWith(".xsl")) {
                    tmpFile = File.createTempFile("currentPC_", ".xml");
                    printToXMLFile(tmpFile, pcExports[loop]);
                    File template = new File(SettingsHandler.getPcgenOutputSheetDir() + File.separator + (String) templateList.getSelectedValue());
                    fh.setInputFile(tmpFile, template);
                    SettingsHandler.setSelectedCharacterPDFOutputSheet(template.getAbsolutePath(), (PlayerCharacter) Globals.getPCList().get(pcExports[loop]));
                } else {
                    tmpFile = File.createTempFile("currentPC_", ".fo");
                    printToFile(tmpFile, pcExports[loop]);
                    fh.setInputFile(tmpFile);
                }
                fh.setMode(FOPHandler.PDF_MODE);
                fh.setOutputFile(outFile);
                Throwable throwable = null;
                timer.start();
                try {
                    Logging.memoryReport();
                    Runtime.getRuntime().gc();
                    Logging.memoryReport();
                    new org.apache.fop.apps.Options();
                    System.out.println("Fop Version: " + org.apache.fop.apps.Version.getVersion());
                    fh.run();
                    Logging.memoryReport();
                    Runtime.getRuntime().gc();
                    Logging.memoryReport();
                } catch (Throwable t) {
                    Logging.memoryReport();
                    throwable = t;
                }
                timer.stop();
                unblock();
                tmpFile.deleteOnExit();
                if (throwable != null) {
                    throwable.printStackTrace();
                    if (throwable instanceof OutOfMemoryError) {
                        StringBuffer errMsg = new StringBuffer("Your character could not be exported as there was not\n" + "enough memory available.\n\n");
                        if (Globals.getPCList().size() > 1) {
                            errMsg.append("To export out your character, please try closing and \n" + "reopening PCGen and then only loading the required PC.");
                        } else {
                            errMsg.append("To export out your character, please try running PCGen\n" + "using the pcgenhighmem.bat file (or an equivalent).");
                        }
                        ShowMessageDelegate.showMessageDialog(errMsg.toString(), "PCGen", MessageType.ERROR);
                        Runtime.getRuntime().gc();
                        Logging.memoryReport();
                    } else {
                        ShowMessageDelegate.showMessageDialog(throwable.getClass().getName() + ": " + throwable.getMessage(), "PCGen", MessageType.ERROR);
                    }
                }
                String errMessage = fh.getErrorMessage();
                if (errMessage.length() > 0) {
                    ShowMessageDelegate.showMessageDialog(errMessage, "PCGen", MessageType.ERROR);
                }
                Globals.executePostExportCommandPDF(characterFileName);
            } catch (IOException ex) {
                Logging.errorPrint("Could not export " + pcName, ex);
                ShowMessageDelegate.showMessageDialog("Could not export " + pcName + ". Try another filename.", "PCGen", MessageType.ERROR);
            }
        }
    }
