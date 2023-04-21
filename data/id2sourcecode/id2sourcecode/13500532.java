    private static File getFile(Shell shell) {
        FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
        saveDialog.setFilterExtensions(new String[] { "*.pdf;", "*.*" });
        saveDialog.setFilterNames(new String[] { "Adobe PDF Files (*.pdf)", "All Files " });
        if (saveDialog.open() == null) {
            return null;
        }
        String fileName = saveDialog.getFileName();
        if (!Utils.endsWith(fileName, ".pdf")) {
            fileName += ".pdf";
        }
        File file = new File(saveDialog.getFilterPath(), fileName);
        if (file.exists()) {
            if (Utils.showMessageBox(shell, "ToDo", "The file " + fileName + " already exists.\nWould you like to overwrite it?", SWT.ICON_WARNING | SWT.YES | SWT.NO) == SWT.NO) {
                return null;
            }
        }
        return file;
    }
