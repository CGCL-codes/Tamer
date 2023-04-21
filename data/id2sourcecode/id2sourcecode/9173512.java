    private void saveCSV() {
        JFileChooser chooser = new JFileChooser("Save evaluation result as CSV");
        chooser.setCurrentDirectory(frame.defaultDirectory);
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists() && (JOptionPane.showConfirmDialog(frame, "File already exists. Overwrite?") != JOptionPane.YES_OPTION)) {
                return;
            }
            frame.saveCSV(chooser.getSelectedFile());
        }
    }
