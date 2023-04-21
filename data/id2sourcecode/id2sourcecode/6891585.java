    private boolean showDialog() throws UserCancelException {
        JFileChooser f = new JFileChooser();
        f.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                if (f.getPath().toUpperCase().endsWith(".PDF") || f.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }

            public String getDescription() {
                return "PDF files (*.pdf)";
            }
        });
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (f.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            FileFilter ff = f.getFileFilter();
            if (ff.getDescription().startsWith("PDF")) {
                if (!f.getSelectedFile().toString().toUpperCase().endsWith(".PDF")) {
                    f.setSelectedFile(new File(f.getSelectedFile().toString() + ".pdf"));
                }
            }
            if (!f.getSelectedFile().exists() || (f.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null, "The file already exists, overwrite?", "Already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
                this.file = f.getSelectedFile().toString();
            }
        } else {
            throw new UserCancelException();
        }
        return this.file != null;
    }
