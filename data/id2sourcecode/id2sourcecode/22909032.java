    private void doManualMerge() {
        try {
            final File mine = status.getConflictWrkFile();
            final File theirs = status.getConflictNewFile();
            if (mine == null && theirs == null) {
                JOptionPane.showMessageDialog(view, jEdit.getProperty("ips.Unable_to_fetch_contents_for_comparison.", "Unable to fetch contents for comparison."), jEdit.getProperty("ips.Error", "Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            view.unsplit();
            DualDiffManager.toggleFor(view);
            Runnable runner = new Runnable() {

                public void run() {
                    final EditPane[] editPanes = view.getEditPanes();
                    mine_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    theirs_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            final EditPane[] editPanes = view.getEditPanes();
                            editPanes[0].setBuffer(jEdit.openFile(view, theirs.getAbsolutePath()));
                            JButton theirs_btn = new JButton(jEdit.getProperty("ips.Keep_this_file", "Keep this file"));
                            theirs_btn.setToolTipText(jEdit.getProperty("ips.When_done_merging,_click_this_button_to_keep_this_file_as_the_merged_file.", "When done merging, click this button to keep this file as the merged file."));
                            theirs_btn.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent ae) {
                                    try {
                                        String buffer_text = editPanes[1].getTextArea().getText();
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
                                                if (hasConflictMarkers(editPanes[0].getTextArea().getText())) {
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
                            theirs_panel.add(theirs_btn);
                            EBComponent handler = new EBComponent() {

                                public void handleMessage(EBMessage message) {
                                    if (message instanceof EditPaneUpdate) {
                                        EditPaneUpdate epu = (EditPaneUpdate) message;
                                        EditPane editPane = epu.getEditPane();
                                        View view = editPane.getView();
                                        if (epu.getWhat() == EditPaneUpdate.DESTROYED) {
                                            editPanes[0].getTextArea().removeTopComponent(theirs_panel);
                                            view.repaint();
                                            EditBus.removeFromBus(this);
                                        }
                                    }
                                }
                            };
                            EditBus.addToBus(handler);
                            editPanes[0].getTextArea().addTopComponent(theirs_panel);
                            view.repaint();
                        }
                    });
                    SwingUtilities.invokeLater(new Runnable() {

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
                    });
                    view.getDockableWindowManager().showDockableWindow("jdiff-lines");
                }
            };
            SwingUtilities.invokeLater(runner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
