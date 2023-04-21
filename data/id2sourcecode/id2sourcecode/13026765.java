    public File showFileDialog(final Component parent, final String suggestedPath, String suggestedName) {
        final JFileChooser fc = new JFileChooser() {

            public void setFileFilter(FileFilter filter) {
                final File sel = getSelectedFile();
                if (filter instanceof ImageFilter && (sel == null || !filter.accept(sel))) {
                    String name = null;
                    String path = null;
                    FileChooserUI ui = getUI();
                    if (ui instanceof BasicFileChooserUI) {
                        BasicFileChooserUI bui = (BasicFileChooserUI) getUI();
                        name = bui.getFileName();
                        path = getCurrentDirectory().getAbsolutePath();
                    } else if (sel != null) {
                        name = sel.getName();
                        path = sel.getParent();
                    }
                    if (name != null) {
                        name = FileUtils.cutOffExtension(name);
                        if (name.length() > 0) {
                            ImageFilter imf = (ImageFilter) filter;
                            final String newName = path + File.separator + name + imf.getExtension();
                            EventQueue.invokeLater(new Runnable() {

                                public void run() {
                                    setSelectedFile(new File(newName));
                                }
                            });
                        }
                    }
                }
                super.setFileFilter(filter);
            }

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
        };
        fc.setMultiSelectionEnabled(false);
        if (suggestedPath != null) {
            fc.setCurrentDirectory(new File(suggestedPath));
        }
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(jpgFilter);
        fc.addChoosableFileFilter(pngFilter);
        if (suggestedName == null) {
            suggestedName = "exported";
        }
        String ext = ((ImageFilter) fc.getFileFilter()).getExtension();
        fc.setSelectedFile(new File(suggestedPath + File.separator + suggestedName + ext));
        fc.setApproveButtonText("Save Image");
        int confirm = fc.showDialog(parent, "Save Image");
        if (confirm == JFileChooser.APPROVE_OPTION) {
            File selFile = fc.getSelectedFile();
            if (selFile != null) {
                final String selName = selFile.getName();
                String selExt = FileUtils.getExtension(selName);
                if ("".equals(selExt)) {
                    final String selPath = selFile.getParent();
                    selExt = ((ImageFilter) fc.getFileFilter()).getExtension();
                    selFile = new File(selPath + File.separator + selName + selExt);
                }
            }
            return selFile;
        } else {
            return null;
        }
    }
