    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(jcpPanel.getCurrentWorkDirectory());
        JCPExportFileFilter.addChoosableFileFilters(chooser);
        if (currentFilter != null) {
            chooser.setFileFilter(currentFilter);
        }
        chooser.setFileView(new JCPFileView());
        while (true) {
            int returnVal = chooser.showSaveDialog(jcpPanel);
            String type = null;
            currentFilter = chooser.getFileFilter();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                type = ((JCPExportFileFilter) currentFilter).getType();
                File outFile = new File(chooser.getSelectedFile().getAbsolutePath() + "." + type);
                boolean dowrite = true;
                if (outFile.exists()) {
                    int value = JOptionPane.showConfirmDialog(jcpPanel, "File already exists. Do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.NO_OPTION) {
                        dowrite = false;
                    }
                }
                if (dowrite) {
                    if (type.equals(JCPExportFileFilter.svg)) {
                        try {
                            JChemPaintModel jcpm = jcpPanel.getJChemPaintModel();
                            ChemModel model = (ChemModel) jcpm.getChemModel();
                            saveAsSVG(model, outFile);
                        } catch (Exception exc) {
                            String error = "Error while writing file: " + exc.getMessage();
                            logger.error(error);
                            logger.debug(exc);
                            JOptionPane.showMessageDialog(jcpPanel, error);
                        }
                    } else {
                        Image awtImage = jcpPanel.takeSnapshot();
                        String filename = outFile.toString();
                        logger.debug("Creating binary image: ", filename);
                        RenderedOp image = JAI.create("AWTImage", awtImage);
                        if (type.equals(JCPExportFileFilter.bmp)) {
                            JAI.create("filestore", image, filename, "BMP", null);
                        } else if (type.equals(JCPExportFileFilter.tiff)) {
                            JAI.create("filestore", image, filename, "TIFF", null);
                        } else if (type.equals(JCPExportFileFilter.jpg)) {
                            JAI.create("filestore", image, filename, "JPEG", new JPEGEncodeParam());
                        } else if (type.equals(JCPExportFileFilter.png)) {
                            JAI.create("filestore", image, filename, "PNG", null);
                        } else {
                            JAI.create("filestore", image, filename, "PNG", null);
                        }
                        logger.debug("Binary image saved to: ", filename);
                    }
                    break;
                }
            } else {
                break;
            }
            jcpPanel.setCurrentWorkDirectory(chooser.getCurrentDirectory());
        }
    }
