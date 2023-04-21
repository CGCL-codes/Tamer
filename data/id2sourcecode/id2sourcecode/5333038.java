    protected File chooseExportFile() {
        if (_exportsTracker == null) {
            _exportsTracker = new RecentFileTracker(1, this.getClass(), "EXPORT_URL");
        }
        if (_exportsFileChooser == null) {
            _exportsFileChooser = new JFileChooser();
            _exportsTracker.applyRecentFolder(_exportsFileChooser);
        }
        final int status = _exportsFileChooser.showSaveDialog(this);
        switch(status) {
            case JFileChooser.APPROVE_OPTION:
                break;
            default:
                return null;
        }
        final File file = _exportsFileChooser.getSelectedFile();
        if (file.exists()) {
            final int continueStatus = JOptionPane.showConfirmDialog(this, "The file: \"" + file.getPath() + "\" already exits!\n Overwrite this file?");
            switch(continueStatus) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                    return chooseExportFile();
                default:
                    return null;
            }
        }
        _exportsTracker.cacheURL(file);
        return file;
    }
