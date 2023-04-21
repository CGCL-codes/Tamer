    public final void doExport() {
        FileDialog outDialog = new FileDialog(this, "Save Log File As...", FileDialog.SAVE);
        outDialog.setVisible(true);
        if (outDialog.getFile() != null) {
            File outFile = new File(outDialog.getDirectory(), outDialog.getFile());
            try {
                generateFile(outFile);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Unable to write file: " + ioe, "Unable to read/write file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
