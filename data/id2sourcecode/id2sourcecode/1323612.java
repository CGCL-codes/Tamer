    private String getFileName(boolean save, String ext) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(lastDirectory == null ? System.getProperty("user.dir") : lastDirectory));
        int result;
        if (save) result = fileChooser.showSaveDialog(MainComponent.this); else result = fileChooser.showOpenDialog(MainComponent.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getPath();
            lastDirectory = fileName;
            lastDirectory = lastDirectory.substring(0, lastDirectory.lastIndexOf(File.separatorChar));
            if (save && fileName.indexOf('.') == -1) fileName += ext;
            if (save && (new File(fileName)).exists()) if (JOptionPane.showConfirmDialog(this, "File " + fileName + " already exists. Overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) return null;
            return fileName;
        } else return null;
    }
