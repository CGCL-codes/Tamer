    private void savePanel(String selectedFile, boolean aShowSavedMessage) {
        final boolean showSavedMessage = aShowSavedMessage;
        if (pngJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.PNG.getExtension())) {
                selectedFile += ImageType.PNG.getExtension();
            }
        } else if (tiffJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.TIFF.getExtension())) {
                selectedFile += ImageType.TIFF.getExtension();
            }
        } else if (pdfJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.PDF.getExtension())) {
                selectedFile += ImageType.PDF.getExtension();
            }
        } else if (svgJRadioButton.isSelected()) {
            if (!selectedFile.endsWith(ImageType.SVG.getExtension())) {
                selectedFile += ImageType.SVG.getExtension();
            }
        }
        boolean saveFile = true;
        if (new File(selectedFile).exists()) {
            int option = JOptionPane.showConfirmDialog(this, "The file " + selectedFile + " already exists. Overwrite?", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                saveFile = false;
            }
        }
        if (saveFile) {
            peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            peptideShakerGUI.setLastSelectedFolder(selectedFile);
            final String finalSelectedFile = selectedFile;
            final ExportGraphicsDialog tempRef = this;
            progressDialog = new ProgressDialogX(this, this, true);
            new Thread(new Runnable() {

                public void run() {
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Saving Figure. Please Wait...");
                    progressDialog.setVisible(true);
                }
            }, "ProgressDialog").start();
            new Thread("SaveFigureThread") {

                @Override
                public void run() {
                    try {
                        peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")));
                        ImageType currentImageType;
                        if (pngJRadioButton.isSelected()) {
                            currentImageType = ImageType.PNG;
                        } else if (tiffJRadioButton.isSelected()) {
                            currentImageType = ImageType.TIFF;
                        } else if (pdfJRadioButton.isSelected()) {
                            currentImageType = ImageType.PDF;
                        } else {
                            currentImageType = ImageType.SVG;
                        }
                        if (chartPanel != null) {
                            Export.exportChart(chartPanel.getChart(), chartPanel.getBounds(), new File(finalSelectedFile), currentImageType);
                        } else {
                            Export.exportComponent(graphicsPanel, graphicsPanel.getBounds(), new File(finalSelectedFile), currentImageType);
                        }
                        peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")));
                        progressDialog.dispose();
                        peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        if (showSavedMessage) {
                            JOptionPane.showMessageDialog(peptideShakerGUI, "Plot saved to " + finalSelectedFile, "Plot Saved", JOptionPane.INFORMATION_MESSAGE);
                            tempRef.dispose();
                        }
                    } catch (IOException e) {
                        tempRef.setVisible(false);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(tempRef, "Unable to export plot: " + e.getMessage(), "Error Exporting Plot", JOptionPane.INFORMATION_MESSAGE);
                        tempRef.dispose();
                    } catch (TranscoderException e) {
                        tempRef.setVisible(false);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(tempRef, "Unable to export plot: " + e.getMessage(), "Error Exporting Plot", JOptionPane.INFORMATION_MESSAGE);
                        tempRef.dispose();
                    }
                }
            }.start();
        }
    }
