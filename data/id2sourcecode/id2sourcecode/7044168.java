    protected void exportForLatex() {
        if (problem != null) {
            viewPanel.fileChooserDialog.setDialogTitle("Export for LaTeX");
            viewPanel.fileChooserDialog.setDialogType(JFileChooser.SAVE_DIALOG);
            viewPanel.fileChooserDialog.resetChoosableFileFilters();
            viewPanel.fileChooserDialog.setFileFilter(latexFilter);
            viewPanel.fileChooserDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            viewPanel.fileChooserDialog.setCurrentDirectory(new File(problem.getFileName().substring(0, problem.getFileName().lastIndexOf(".")) + ".Rnw"));
            viewPanel.fileChooserDialog.setSelectedFile(new File(problem.getFileName().substring(0, problem.getFileName().lastIndexOf(".")) + ".Rnw"));
            int response = viewPanel.fileChooserDialog.showSaveDialog(Domain.getTopWindow());
            while (response == JFileChooser.APPROVE_OPTION) {
                File file = viewPanel.fileChooserDialog.getSelectedFile();
                if (file.getName().indexOf(".") == -1) {
                    file = new File(viewPanel.fileChooserDialog.getSelectedFile().toString() + ".Rnw");
                }
                final File finalFile = file;
                if (!finalFile.toString().toLowerCase().endsWith(".rnw")) {
                    Domain.showWarningDialog(Domain.getTopWindow(), "The extension for the file must be .Rnw.", "Invalid Extension");
                    viewPanel.fileChooserDialog.setSelectedFile(new File(viewPanel.fileChooserDialog.getSelectedFile().toString().substring(0, viewPanel.fileChooserDialog.getSelectedFile().toString().lastIndexOf(".")) + ".Rnw"));
                    response = viewPanel.fileChooserDialog.showSaveDialog(Domain.getTopWindow());
                    continue;
                }
                boolean continueAllowed = true;
                if (finalFile.exists()) {
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
                    Domain.setProgressStatus("Beginning LaTeX export...");
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            String filePath = null;
                            try {
                                for (int i = 0; i < problem.getDataCount(); i++) {
                                    List<Operation> ops = problem.getData(i).getAllLeafOperations();
                                    for (Operation op : ops) {
                                        ensureRequirementsMet(op);
                                    }
                                }
                                LatexExporter exporter = new LatexExporter(problem);
                                File genFile = new File(exporter.cleanExport(finalFile.getPath()));
                                filePath = genFile.getCanonicalPath();
                                if (desktop != null) {
                                    Domain.setProgressStatus("Opening LaTeX RNW...");
                                    desktop.open(genFile);
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException ex) {
                                    }
                                } else {
                                    throw new IOException();
                                }
                            } catch (IOException ex) {
                                if (filePath != null) {
                                    Domain.setProgressIndeterminate(false);
                                    Domain.showInformationDialog(Domain.getTopWindow(), "The file was exported successfully.\nLocation: " + filePath, "Export Successful");
                                } else {
                                    Domain.logger.add(ex);
                                    Domain.showErrorDialog(Domain.getTopWindow(), ex.getMessage(), Domain.prettyExceptionDetails(ex), "Export Failed");
                                }
                            } catch (MarlaException ex) {
                                Domain.logger.add(ex);
                                Domain.showErrorDialog(Domain.getTopWindow(), ex.getMessage(), Domain.prettyExceptionDetails(ex), "Export Failed");
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
