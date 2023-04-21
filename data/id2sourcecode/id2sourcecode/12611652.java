    public Object call() {
        File file = null;
        JFileChooser chooser = new JFileChooser();
        for (FileFilter f : JDMPFileFilter.getChoosableFileFilters(getObject())) {
            chooser.addChoosableFileFilter(f);
        }
        for (FileFilter f : JDMPFileFilter.getChoosableFileFilters(getComponent())) {
            chooser.addChoosableFileFilter(f);
        }
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Export");
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            FileFilter filter = chooser.getFileFilter();
            String suffix = ((JDMPFileFilter) filter).getSuffix();
            if (!file.getAbsolutePath().toLowerCase().endsWith(suffix)) {
                file = new File(file.getAbsolutePath() + suffix);
            }
        }
        if (file == null) return null;
        if (file.exists()) {
            int result = JOptionPane.showConfirmDialog(null, "File already exists. Overwrite?", "Warning", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) return null;
        }
        return null;
    }
