    public boolean doSaveAsFile() {
        JFileChooser chooser;
        if (lastPath == null) {
            chooser = new JFileChooser();
        } else {
            File fiTmp = new File(lastPath);
            chooser = new JFileChooser(fiTmp);
        }
        MapFileFilter filter = new MapFileFilter();
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fiSelected = chooser.getSelectedFile();
            if (fiSelected.exists()) {
                Object[] options = { "Yes", "No" };
                int n = JOptionPane.showOptionDialog(this, "The file already exists, would you like to overwrite?", "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 1) {
                    return false;
                }
            }
            lastPath = fiSelected.getPath();
            ttd.setFileName(fiSelected.getAbsolutePath());
            ttd.doSave();
            ttd.setHasChanged(false);
            return true;
        }
        return false;
    }
