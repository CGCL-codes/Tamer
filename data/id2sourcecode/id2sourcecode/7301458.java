    public File checkOverwrite() {
        JFileChooser jfc = new JFileChooser(cwd);
        jfc.setDialogTitle("Select existing or create new file to save properties");
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = jfc.showSaveDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().exists()) {
                int opRet = JOptionPane.showOptionDialog(null, "File " + jfc.getSelectedFile().getAbsolutePath() + "\nalready exists, overwrite?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Overwrite", "Cancel" }, "Cancel");
                if (opRet == JOptionPane.YES_OPTION) {
                    return jfc.getSelectedFile();
                } else {
                    cwd = jfc.getSelectedFile().getParentFile();
                    return checkOverwrite();
                }
            }
            return jfc.getSelectedFile();
        }
        return null;
    }
