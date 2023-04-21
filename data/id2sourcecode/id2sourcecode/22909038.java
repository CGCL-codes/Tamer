                        public void run() {
                            final EditPane[] editPanes = view.getEditPanes();
                            editPanes[1].setBuffer(jEdit.openFile(view, mine.getAbsolutePath()));
                            JButton mine_btn = new JButton(jEdit.getProperty("ips.Keep_this_file", "Keep this file"));
                            mine_btn.setToolTipText(jEdit.getProperty("ips.When_done_merging,_click_this_button_to_keep_this_file_as_the_merged_file.", "When done merging, click this button to keep this file as the merged file."));
                            mine_btn.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent ae) {
                                    try {
                                        String buffer_text = editPanes[0].getTextArea().getText();
                                        StringReader reader = new StringReader(buffer_text);
                                        FileWriter writer = new FileWriter(status.getFile(), false);
                                        FileUtilities.copy(reader, writer);
                                        jEdit._closeBuffer(view, jEdit.getBuffer(mine.getAbsolutePath()));
                                        jEdit._closeBuffer(view, jEdit.getBuffer(theirs.getAbsolutePath()));
                                        jEdit._closeBuffer(view, jEdit.getBuffer(status.getFile().getAbsolutePath()));
                                        editPanes[0].getTextArea().removeTopComponent(mine_panel);
                                        editPanes[1].getTextArea().removeTopComponent(theirs_panel);
                                        DualDiffManager.toggleFor(view);
                                        Runnable r2d2 = new Runnable() {

                                            public void run() {
                                                view.unsplit();
                                                jEdit.openFile(view, status.getFile().getAbsolutePath());
                                                if (hasConflictMarkers(editPanes[1].getTextArea().getText())) {
                                                    int rtn = JOptionPane.showConfirmDialog(view, jEdit.getProperty("ips.This_file_appears_to_contain_SVN_conflict_markers.", "This file appears to contain SVN conflict markers.") + "\n" + jEdit.getProperty("ips.Are_you_sure_you_want_to_use_this_file_as_is?", "Are you sure you want to use this file as is?"), jEdit.getProperty("ips.Possible_Conflict_Markers", "Possible Conflict Markers"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                                    if (rtn == JOptionPane.NO_OPTION) {
                                                        return;
                                                    }
                                                }
                                                ResolveConflictDialog.this.resolve();
                                            }
                                        };
                                        SwingUtilities.invokeLater(r2d2);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            mine_panel.add(mine_btn);
                            EBComponent handler = new EBComponent() {

                                public void handleMessage(EBMessage message) {
                                    if (message instanceof EditPaneUpdate) {
                                        EditPaneUpdate epu = (EditPaneUpdate) message;
                                        EditPane editPane = epu.getEditPane();
                                        View view = editPane.getView();
                                        if (epu.getWhat() == EditPaneUpdate.DESTROYED) {
                                            editPanes[1].getTextArea().removeTopComponent(mine_panel);
                                            view.repaint();
                                            EditBus.removeFromBus(this);
                                        }
                                    }
                                }
                            };
                            EditBus.addToBus(handler);
                            editPanes[1].getTextArea().addTopComponent(mine_panel);
                            view.repaint();
                        }
