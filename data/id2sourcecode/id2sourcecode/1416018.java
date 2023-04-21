    public void saveAs() {
        final JFileChooser fileChooser = this.createFileChooser();
        final int result = fileChooser.showSaveDialog(this.getWindow());
        if (result == JFileChooser.APPROVE_OPTION) {
            final File file = fileChooser.getSelectedFile();
            final String suffix = "." + this.getDefaultFileExtension();
            final File correctedFile;
            if (file.getName().endsWith(suffix)) {
                correctedFile = file;
            } else {
                correctedFile = new File(file.getPath() + suffix);
            }
            if (correctedFile.exists()) {
                String[] options = { "Replace", "Cancel" };
                final int replace = JOptionPane.showOptionDialog(this.getWindow(), "\"" + correctedFile.getName() + "\" already exists. Do you want to replace it?  Replacing it will overwrite its current contents.", null, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (replace == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            try {
                this.writeData(correctedFile);
                this.setCurrentFile(correctedFile);
                this.getUndoController().markChangesSaved();
            } catch (IOException e) {
                log().error("Unable to save file", e);
                this.runFileWriteErrorMessage(file, e.getLocalizedMessage());
            }
        }
    }
