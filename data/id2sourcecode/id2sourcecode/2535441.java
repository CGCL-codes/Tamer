    private void saveAs() {
        FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
        saveDialog.setFilterExtensions(new String[] { "*.tdl;", "*.*" });
        saveDialog.setFilterNames(new String[] { "ToDo Lists (*.tdl)", "All Files " });
        if (saveDialog.open() == null) return;
        String fileName = saveDialog.getFileName();
        if (!Utils.endsWith(fileName, ".tdl")) {
            fileName += ".tdl";
        }
        File file = new File(saveDialog.getFilterPath(), fileName);
        if (file.exists()) {
            int choice = Utils.showMessageBox(shell, "ToDo", "The file " + fileName + " already exists.\nWould you like to overwrite it?", SWT.ICON_WARNING | SWT.OK | SWT.NO);
            if (choice == SWT.NO) {
                return;
            }
        }
        currentFile = file;
        currentFilename = fileName;
        save();
    }
