            public void approveSelection() {
                final File selFile = getSelectedFile();
                if (selFile.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(parent, "File '" + selFile.getName() + "' already exists.\n" + "Would you like to overwrite it?", "Confirm overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (overwrite != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                super.approveSelection();
            }
