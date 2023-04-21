    private void saveLTLFileAs() {
        Set<String> formulas = getFormulasToSave();
        if (formulas != null) {
            JFileChooser chooser = new JFileChooser(this.parser.getFilename());
            if (this.parser.getFilename() != null) {
                chooser.setSelectedFile(new File(this.parser.getFilename()));
            }
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (f.exists()) {
                    if (f.isFile()) {
                        if (JOptionPane.showConfirmDialog(this, "File already exists: " + f.getPath() + "\n\nOverwrite existing file?", "File exists", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a filename, not a directory.");
                        return;
                    }
                }
                saveLTLFile(f.getPath(), formulas);
            }
        }
    }
