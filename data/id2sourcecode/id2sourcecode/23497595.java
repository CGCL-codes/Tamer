    private boolean showDialog() throws UserCancelException, IOException {
        String file = null;
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
            if (!f.getSelectedFile().toString().toUpperCase().endsWith(".PDF")) {
                f.setSelectedFile(new File(f.getSelectedFile().toString() + ".pdf"));
            }
            if (!f.getSelectedFile().exists() || (f.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null, "The file already exists, overwrite?", "Already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
                file = f.getSelectedFile().toString();
            }
        } else {
            throw new UserCancelException();
        }
        this.out = new FileOutputStream(file);
        return file != null;
    }
