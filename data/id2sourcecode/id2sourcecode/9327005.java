    protected void saveAs(ActionEvent event) {
        int ready = 1;
        while (ready == 1) {
            JChemPaintModel jcpm = jcpPanel.getJChemPaintModel();
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(jcpPanel.getCurrentWorkDirectory());
            JCPSaveFileFilter.addChoosableFileFilters(chooser);
            if (jcpPanel.getCurrentSaveFileFilter() != null) {
            }
            chooser.setFileView(new JCPFileView());
            int returnVal = chooser.showSaveDialog(jcpPanel);
            IChemObject object = getSource(event);
            currentFilter = chooser.getFileFilter();
            if (returnVal == JFileChooser.CANCEL_OPTION) ready = 0;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                type = ((IJCPFileFilter) currentFilter).getType();
                File outFile = chooser.getSelectedFile();
                if (outFile.exists()) {
                    ready = JOptionPane.showConfirmDialog((Component) null, "File " + outFile.getName() + " already exists. Do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
                } else {
                    try {
                        if (new File(outFile.getCanonicalFile() + "." + type).exists()) {
                            ready = JOptionPane.showConfirmDialog((Component) null, "File " + outFile.getName() + " already exists. Do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
                        }
                    } catch (IOException ex) {
                        logger.error("IOException when trying to ask for existing file");
                    }
                    ready = 0;
                }
                if (ready == 0) {
                    IChemModel model = (IChemModel) jcpm.getChemModel();
                    if (object == null) {
                        try {
                            if (type.equals(JCPSaveFileFilter.mol)) {
                                outFile = saveAsMol(model, outFile);
                            } else if (type.equals(JCPSaveFileFilter.cml)) {
                                outFile = saveAsCML2(model, outFile);
                            } else if (type.equals(JCPSaveFileFilter.smiles)) {
                                outFile = saveAsSMILES(model, outFile);
                            } else if (type.equals(JCPSaveFileFilter.svg)) {
                                outFile = saveAsSVG(model, outFile);
                            } else if (type.equals(JCPSaveFileFilter.cdk)) {
                                outFile = saveAsCDKSourceCode(model, outFile);
                            } else {
                                String error = "Cannot save file in this format: " + type;
                                logger.error(error);
                                JOptionPane.showMessageDialog(jcpPanel, error);
                                return;
                            }
                            jcpm.resetIsModified();
                        } catch (Exception exc) {
                            String error = "Error while writing file: " + exc.getMessage();
                            logger.error(error);
                            logger.debug(exc);
                            JOptionPane.showMessageDialog(jcpPanel, error);
                        }
                    } else if (object instanceof Reaction) {
                        try {
                            if (type.equals(JCPSaveFileFilter.cml)) {
                                outFile = saveAsCML2(object, outFile);
                            } else {
                                String error = "Cannot save reaction in this format: " + type;
                                logger.error(error);
                                JOptionPane.showMessageDialog(jcpPanel, error);
                            }
                        } catch (Exception exc) {
                            String error = "Error while writing file: " + exc.getMessage();
                            logger.error(error);
                            logger.debug(exc);
                            JOptionPane.showMessageDialog(jcpPanel, error);
                        }
                    }
                    jcpPanel.setCurrentWorkDirectory(chooser.getCurrentDirectory());
                    jcpPanel.setCurrentSaveFileFilter(chooser.getFileFilter());
                    jcpPanel.setIsAlreadyAFile(outFile);
                    jcpPanel.getJChemPaintModel().setTitle(outFile.getName());
                    ((JFrame) jcpPanel.getParent().getParent().getParent().getParent()).setTitle(outFile.getName());
                }
            }
        }
    }
