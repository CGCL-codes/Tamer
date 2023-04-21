                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        JFileChooser jfc;
                        if (lastOutputDir != null) {
                            jfc = new JFileChooser(lastOutputDir);
                        } else {
                            jfc = new JFileChooser();
                        }
                        jfc.setFileFilter(new FileFilter() {

                            @Override
                            public boolean accept(File f) {
                                return f.isDirectory() || (f.isFile() && f.getName().matches(HTML_PAGE_FILE_NAME_PATTERN));
                            }

                            @Override
                            public String getDescription() {
                                return "HTML page (*.htm, *.html)";
                            }
                        });
                        jfc.setMultiSelectionEnabled(false);
                        File file;
                        String fileName = FrontEnd.getSelectedVisualEntryCaption();
                        if (!Validator.isNullOrBlank(fileName)) {
                            file = new File(fileName);
                            jfc.setSelectedFile(file);
                        }
                        if (jfc.showSaveDialog(HTMLEditorPanel.this) == JFileChooser.APPROVE_OPTION) {
                            file = jfc.getSelectedFile();
                            if (!file.getName().matches(HTML_PAGE_FILE_NAME_PATTERN)) {
                                file = new File(file.getParentFile(), file.getName() + ".html");
                            }
                            Integer option = null;
                            if (file.exists()) {
                                option = JOptionPane.showConfirmDialog(HTMLEditorPanel.this, "File already exists, overwrite?", "Overwrite existing file", JOptionPane.YES_NO_OPTION);
                            }
                            if (option == null || option == JOptionPane.YES_OPTION) {
                                saveToFile(file, getJTextPane().getText());
                                lastOutputDir = file.getParentFile();
                            }
                        }
                    } catch (Exception ex) {
                        FrontEnd.displayErrorMessage(ex);
                    }
                }
