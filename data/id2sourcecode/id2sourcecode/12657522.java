    private String chooseSavePath() {
        if (save_file_chooser == null) {
            save_file_chooser = new JFileChooser();
            save_file_chooser.setCurrentDirectory(new File("."));
            save_file_chooser.setFileFilter(new FileFilterXML());
        }
        String path = null;
        int dialog_result = save_file_chooser.showSaveDialog(this);
        if (dialog_result == JFileChooser.APPROVE_OPTION) {
            File to_save_to = save_file_chooser.getSelectedFile();
            path = to_save_to.getPath();
            String ext = jAudioFeatureExtractor.GeneralTools.StringMethods.getExtension(path);
            if (ext == null) {
                path += ".xml";
                to_save_to = new File(path);
            } else if (!ext.equals(".xml")) {
                path = jAudioFeatureExtractor.GeneralTools.StringMethods.removeExtension(path) + ".xml";
                to_save_to = new File(path);
            }
            if (to_save_to.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(null, "This file already exists.\nDo you wish to overwrite it?", "WARNING", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) path = null;
            }
        }
        return path;
    }
