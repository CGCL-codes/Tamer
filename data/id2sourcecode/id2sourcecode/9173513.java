    private void saveReferenceXML() {
        JFileChooser chooser = new JFileChooser("Save as alignment xml format. YOUR EVALUATION WILL NOT BE SAVED, ONLY A COPY OF THE INPUT.");
        chooser.setCurrentDirectory(new File("."));
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists() && (JOptionPane.showConfirmDialog(frame, "File already exists. Overwrite?") != JOptionPane.YES_OPTION)) {
                return;
            }
            frame.saveReferenceXML(chooser.getSelectedFile(), true);
        }
    }
