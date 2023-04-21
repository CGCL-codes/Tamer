    public void saveAsActiveTab() {
        final String activePath = getActiveFilePath();
        final JEditTextArea activeTextArea = getActiveTextArea();
        if (activeTextArea == null) {
            return;
        }
        final String text = activeTextArea.getText();
        final int tabIndex = view.getSelectedIndex();
        openFileChooser.setDialogTitle("Save Script File As");
        if (activePath != null) {
            final File file = new File(activePath);
            if (file.getParentFile().exists() && file.getParentFile().isDirectory()) {
                openFileChooser.setCurrentDirectory(file.getParentFile());
                openFileChooser.setSelectedFile(file);
            }
        }
        final int returnVal = openFileChooser.showSaveDialog(view);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = openFileChooser.getSelectedFile();
        if (!file.getName().endsWith(scriptSuffix)) {
            final String fileName = file.getAbsolutePath();
            file = new File(fileName + scriptSuffix);
        }
        if (!file.exists() || (activePath != null && file.getAbsolutePath().equals(activePath)) || view.askConfirm("Overwrite?", "A file named \"" + file.getName() + "\" already exists.\n" + "Are you sure you want to overwrite it?")) {
            if (saveTextToFile(file, text)) {
                activeTextArea.resetModified();
            }
            if (tabIndex >= 0 && tabs.size() > tabIndex) {
                tabs.set(tabIndex, file.getAbsolutePath());
                view.setTitleAt(tabIndex, file.getName());
            }
        }
    }
