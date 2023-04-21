            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileFilter(new XML_Filter(""));
                chooser.setDialogTitle(GUIMessages.getString("GUI.xmlsavedialog"));
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    if (!fileName.substring(fileName.length() - 4, fileName.length()).equals(".xml")) {
                        fileName += ".xml";
                    }
                    try {
                        File sortie = new File(fileName);
                        if (sortie.exists()) {
                            int overwrite = DialogBox.showDialogWarning(panel, GUIMessages.getString("GUI.doYouWantToOverwriteFile"), GUIMessages.getString("GUI.selectedFileAlreadyExist"));
                            if (overwrite == DialogBox.NO) {
                                DialogBox.showMessageInformation(panel, GUIMessages.getString("GUI.contextHasNotBeenSaved"), GUIMessages.getString("GUI.notSaved"));
                            } else if (overwrite == DialogBox.YES) {
                                @SuppressWarnings("unused") RulesWriter exporter = new RulesWriter(sortie, rules, minSupport, minConfidence, contextName, panel);
                            }
                        } else {
                            @SuppressWarnings("unused") RulesWriter exporter = new RulesWriter(sortie, rules, minSupport, minConfidence, contextName, panel);
                        }
                    } catch (NoClassDefFoundError e1) {
                        JOptionPane.showMessageDialog(panel, GUIMessages.getString("GUI.jdomLibraryMissing"), GUIMessages.getString("GUI.notSaved"), JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        System.err.println("I/O Error");
                    }
                }
            }
