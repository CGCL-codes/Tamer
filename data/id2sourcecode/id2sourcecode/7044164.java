    protected void exportDataSet() {
        Object[] dataSets = new Object[problem.getDataCount()];
        for (int i = 0; i < problem.getDataCount(); ++i) {
            dataSets[i] = problem.getData(i).getName();
        }
        final Object resp = Domain.showComboDialog(Domain.getTopWindow(), "Select the data set you would like to export:", dataSets, "Export Data Set", null);
        if (resp != null) {
            viewPanel.fileChooserDialog.setDialogTitle("Export Data Set");
            viewPanel.fileChooserDialog.setDialogType(JFileChooser.SAVE_DIALOG);
            viewPanel.fileChooserDialog.resetChoosableFileFilters();
            viewPanel.fileChooserDialog.setFileFilter(viewPanel.newProblemWizardDialog.csvFilter);
            viewPanel.fileChooserDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            viewPanel.fileChooserDialog.setCurrentDirectory(new File(Domain.lastGoodDir));
            viewPanel.fileChooserDialog.setSelectedFile(new File(Domain.lastGoodDir, resp.toString()));
            int response = viewPanel.fileChooserDialog.showSaveDialog(Domain.getTopWindow());
            while (response == JFileChooser.APPROVE_OPTION) {
                File file = viewPanel.fileChooserDialog.getSelectedFile();
                if (file.getName().indexOf(".") == -1) {
                    file = new File(viewPanel.fileChooserDialog.getSelectedFile().toString() + ".csv");
                }
                final File finalFile = file;
                if (!finalFile.toString().toLowerCase().endsWith(".csv")) {
                    Domain.showWarningDialog(Domain.getTopWindow(), "The extension for the file must be .csv.", "Invalid Extension");
                    viewPanel.fileChooserDialog.setSelectedFile(new File(viewPanel.fileChooserDialog.getSelectedFile().toString().substring(0, viewPanel.fileChooserDialog.getSelectedFile().toString().lastIndexOf(".")) + ".csv"));
                    response = viewPanel.fileChooserDialog.showSaveDialog(Domain.getTopWindow());
                    continue;
                }
                boolean continueAllowed = true;
                if (file.exists()) {
                    response = Domain.showConfirmDialog(Domain.getTopWindow(), "The selected file already exists.\nWould you like to overwrite the existing file?", "Overwrite Existing File", JOptionPane.YES_NO_OPTION);
                    if (response != JOptionPane.YES_OPTION) {
                        continueAllowed = false;
                    }
                }
                if (continueAllowed) {
                    Domain.setProgressTitle("Exporting");
                    Domain.setProgressVisible(true);
                    Domain.setProgressIndeterminate(true);
                    Domain.setProgressString("");
                    Domain.setProgressStatus("Beginning CSV export...");
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            String filePath = null;
                            try {
                                problem.getData(resp.toString()).exportFile(finalFile.getCanonicalPath());
                                filePath = finalFile.getCanonicalPath();
                                if (desktop != null) {
                                    Domain.setProgressStatus("Opening CSV...");
                                    desktop.open(new File(finalFile.getCanonicalPath()));
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                    }
                                }
                            } catch (IOException ex) {
                                if (filePath != null) {
                                    Domain.setProgressIndeterminate(false);
                                    Domain.showInformationDialog(Domain.getTopWindow(), "The file was exported successfully.\nLocation: " + filePath, "Export Successful");
                                } else {
                                    Domain.logger.add(ex);
                                    Domain.showErrorDialog(Domain.getTopWindow(), ex.getMessage(), Domain.prettyExceptionDetails(ex), "PDF Export Failed");
                                }
                            } finally {
                                Domain.setProgressVisible(false);
                                Domain.setProgressIndeterminate(false);
                            }
                        }
                    }).start();
                    break;
                } else {
                    continue;
                }
            }
        }
    }
