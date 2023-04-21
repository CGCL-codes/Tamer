    public boolean saveAs() {
        JFileChooser chooser = null;
        if ((getMap().getFile() != null) && (getMap().getFile().getParentFile() != null)) {
            chooser = new JFileChooser(getMap().getFile().getParentFile());
        } else {
            chooser = new JFileChooser();
            chooser.setSelectedFile(new File(((MindMapNode) getMap().getRoot()).toString() + ".mm"));
        }
        if (getFileFilter() != null) {
            chooser.addChoosableFileFilter(getFileFilter());
        }
        chooser.setDialogTitle(getText("save_as"));
        int returnVal = chooser.showSaveDialog(getView());
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        File f = chooser.getSelectedFile();
        String ext = Tools.getExtension(f.getName());
        if (!ext.equals("mm")) {
            f = new File(f.getParent(), f.getName() + ".mm");
        }
        if (f.exists()) {
            int overwriteMap = JOptionPane.showConfirmDialog(getView(), getText("map_already_exists"), "FreeMindAdapter", JOptionPane.YES_NO_OPTION);
            if (overwriteMap != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        try {
            String lockingUser = getModel().tryToLock(f);
            if (lockingUser != null) {
                getFrame().getController().informationMessage(Tools.expandPlaceholders(getText("map_locked_by_save_as"), f.getName(), lockingUser));
                return false;
            }
        } catch (Exception e) {
            getFrame().getController().informationMessage(Tools.expandPlaceholders(getText("locking_failed_by_save_as"), f.getName()));
            return false;
        }
        save(f);
        getController().getMapModuleManager().updateMapModuleName();
        return true;
    }
