    protected File getfile() {
        if (exportsTracker == null) {
            exportsTracker = new RecentFileTracker(1, doc.myWindow().getClass(), "EXPORT_URL");
        }
        if (exportsFileChooser == null) {
            exportsFileChooser = new JFileChooser();
            exportsTracker.applyRecentFolder(exportsFileChooser);
        }
        final int status = exportsFileChooser.showSaveDialog(doc.myWindow());
        switch(status) {
            case JFileChooser.APPROVE_OPTION:
                break;
            default:
                return null;
        }
        final File file = exportsFileChooser.getSelectedFile();
        if (file.exists()) {
            final int continueStatus = JOptionPane.showConfirmDialog(doc.myWindow(), "The file: \"" + file.getPath() + "\" already exits!\n Overwrite this file?");
            switch(continueStatus) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                    return getfile();
                default:
                    return null;
            }
        }
        exportsTracker.cacheURL(file);
        return file;
    }
