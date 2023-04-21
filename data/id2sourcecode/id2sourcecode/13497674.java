            public void actionPerformed(ActionEvent evt) {
                int[] ind = jt.getSelectedRows();
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setMultiSelectionEnabled(false);
                jfc.setDialogTitle("Save as");
                for (int i = 0; i < ind.length; i++) {
                    OpenFile of = ofm.getFile(ind[i]);
                    if (of.getContentName() != null) {
                        jfc.setSelectedFile(new File(jfc.getCurrentDirectory(), of.getContentName()));
                        if (jfc.showSaveDialog(MainWindow.this) != JFileChooser.APPROVE_OPTION) {
                            return;
                        }
                        if (jfc.getSelectedFile().exists()) {
                            int res = JOptionPane.showConfirmDialog(MainWindow.this, "Really overwrite?", "File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (res != JOptionPane.YES_OPTION) {
                                continue;
                            }
                        }
                        showWaitCursor();
                        controller.fetch(of, jfc.getSelectedFile());
                        showNormalCursor();
                    }
                }
            }
