    private void saveSrtFileAs() {
        while ((srtFileChooser.showSaveDialog(this)) == JFileChooser.APPROVE_OPTION) {
            File srt = SrtToolkit.getSelectedSrtFile(srtFileChooser);
            if (srt.exists()) {
                int option = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                switch(option) {
                    case JOptionPane.YES_OPTION:
                        break;
                    case JOptionPane.NO_OPTION:
                        continue;
                    case JOptionPane.CANCEL_OPTION:
                        return;
                }
            }
            try {
                SrtToolkit.saveSrtFile(srtTableModel.getSubtitles(), srt, Context.getCharset());
                break;
            } catch (IOException ex) {
                SwingToolkit.showRootCause(ex, this);
            }
        }
    }
