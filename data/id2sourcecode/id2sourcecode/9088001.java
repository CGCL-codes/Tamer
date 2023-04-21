    @Override
    public void approveSelection() {
        File selectedFile = getSelectedFile();
        if (selectedFile.getName().indexOf('.') == -1) {
            final FileFilter filter = getFileFilter();
            if (filter instanceof ExtensionFileFilter) {
                final ExtensionFileFilter extFileFilter = (ExtensionFileFilter) filter;
                final String[] extensions = extFileFilter.getExtensions();
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + extensions[0]);
                setSelectedFile(selectedFile);
            }
        }
        if (selectedFile.exists()) {
            final String warningMessage = "The file '" + selectedFile.getName() + "' already exists.\nDo you want to overwrite the existing file?";
            final int response = JOptionPane.showOptionDialog(this, warningMessage, "File Export Warning: File exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[] { "Yes", "No" }, "No");
            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        }
        super.approveSelection();
    }
