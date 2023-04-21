    private void saveXML() {
        JFileChooser chooser = new JFileChooser("Save evaluation result as alignment XML");
        chooser.setCurrentDirectory(frame.defaultDirectory);
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists() && (JOptionPane.showConfirmDialog(frame, "File already exists. Overwrite?") != JOptionPane.YES_OPTION)) {
                return;
            }
            frame.saveXML(chooser.getSelectedFile(), SaveXMLMode.SAVE_EVERYTHING);
        }
    }
