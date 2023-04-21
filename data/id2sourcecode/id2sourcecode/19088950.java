    public void extractPagesToNewPDF(SavePDF current_selection) {
        final boolean exportIntoMultiplePages = current_selection.getExportType();
        final int[] pgsToExport = current_selection.getExportPages();
        if (pgsToExport == null) return;
        final int noOfPages = pgsToExport.length;
        final String output_dir = current_selection.getRootDir() + separator + fileName + separator + "PDFs" + separator;
        File testDirExists = new File(output_dir);
        if (!testDirExists.exists()) testDirExists.mkdirs();
        final ProgressMonitor status = new ProgressMonitor(currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.GeneratingPdfs"), "", 0, noOfPages);
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                if (exportIntoMultiplePages) {
                    boolean yesToAll = false;
                    for (int i = 0; i < noOfPages; i++) {
                        int page = pgsToExport[i];
                        if (status.isCanceled()) {
                            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") + i + " " + Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                            return null;
                        }
                        try {
                            PdfReader reader = new PdfReader(selectedFile);
                            File fileToSave = new File(output_dir + fileName + "_pg_" + page + ".pdf");
                            if (fileToSave.exists() && !yesToAll) {
                                if (pgsToExport.length > 1) {
                                    int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(), true);
                                    if (n == 0) {
                                    } else if (n == 1) {
                                        yesToAll = true;
                                    } else if (n == 2) {
                                        status.setProgress(page);
                                        continue;
                                    } else {
                                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") + i + " " + Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                                        status.close();
                                        return null;
                                    }
                                } else {
                                    int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(), false);
                                    if (n == 0) {
                                    } else {
                                        return null;
                                    }
                                }
                            }
                            Document document = new Document();
                            PdfCopy writer = new PdfCopy(document, new FileOutputStream(fileToSave));
                            document.open();
                            PdfImportedPage pip = writer.getImportedPage(reader, page);
                            writer.addPage(pip);
                            PRAcroForm form = reader.getAcroForm();
                            if (form != null) {
                                writer.copyAcroForm(reader);
                            }
                            document.close();
                        } catch (Exception de) {
                            de.printStackTrace();
                        }
                        status.setProgress(i + 1);
                    }
                } else {
                    try {
                        PdfReader reader = new PdfReader(selectedFile);
                        File fileToSave = new File(output_dir + "export_" + fileName + ".pdf");
                        if (fileToSave.exists()) {
                            int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(), false);
                            if (n == 0) {
                            } else {
                                return null;
                            }
                        }
                        Document document = new Document();
                        PdfCopy copy = new PdfCopy(document, new FileOutputStream(fileToSave.getAbsolutePath()));
                        document.open();
                        PdfImportedPage pip;
                        for (int i = 0; i < noOfPages; i++) {
                            int page = pgsToExport[i];
                            pip = copy.getImportedPage(reader, page);
                            copy.addPage(pip);
                        }
                        PRAcroForm form = reader.getAcroForm();
                        if (form != null) {
                            copy.copyAcroForm(reader);
                        }
                        List bookmarks = SimpleBookmark.getBookmark(reader);
                        copy.setOutlines(bookmarks);
                        document.close();
                    } catch (Exception de) {
                        de.printStackTrace();
                    }
                }
                status.close();
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.PagesSavedAsPdfTo") + " " + output_dir);
                return null;
            }
        };
        worker.start();
    }
