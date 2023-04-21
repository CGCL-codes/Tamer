    private void exportSubtitles() {
        int beginIndex = 0;
        int endIndex = 0;
        try {
            beginIndex = Integer.parseInt(beginIndexTextField.getText());
            endIndex = Integer.parseInt(endIndexTextField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Begin and end indexex must be integers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (beginIndex > endIndex) {
            JOptionPane.showMessageDialog(this, "Begin index mustn't be greater than end index", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (beginIndex < 1) {
            JOptionPane.showMessageDialog(this, "Begin index mustn't be smaller than 1.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File targetFile = new File(targetFileTextField.getText());
        if (targetFile.exists()) {
            int option = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            switch(option) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                default:
                    return;
            }
        }
        firePropertyChange(EXPORT_SUBTITLES_PROPERTY, null, new SubtitleExporter(beginIndex, endIndex, targetFile));
        dispose();
    }
