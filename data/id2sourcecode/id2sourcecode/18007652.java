    private void runSaveAs() {
        OScriptEditor e = PlatoEditorUtil.getActiveOScriptEditor();
        if (e == null) {
            return;
        }
        Shell shell = PlatoPluginUtil.getShell();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE | SWT.SYSTEM_MODAL);
        dialog.setText("Save As...");
        String OS_EXT = ".os";
        dialog.setFilterExtensions(new String[] { "*" + OS_EXT, "*" });
        dialog.setFilterNames(new String[] { "Object Script File (*" + OS_EXT + ")", "All Files (*)" });
        File currentFile = e.getEditedFile();
        if (currentFile == null) {
            dialog.setFileName("Untitled" + OS_EXT);
        } else {
            dialog.setFileName(currentFile.getAbsolutePath());
        }
        dialog.setFilterPath(System.getProperty("user.home") + File.separator + "Desktop");
        final String name = dialog.open();
        if (name == null || name.equals("")) {
            return;
        }
        File newFile = addFileExtension(currentFile, name);
        if (currentFile.equals(newFile)) {
            return;
        }
        boolean isOverride = true;
        if (newFile.exists() && !currentFile.equals(newFile)) {
            MessageBox mbox = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING | SWT.APPLICATION_MODAL);
            mbox.setText("Warning");
            mbox.setMessage("This file already exists. Do you want to overwrite it?");
            if (mbox.open() == SWT.CANCEL) {
                isOverride = false;
            }
        }
        boolean isSaveChanges = true;
        if (e.isDirty()) {
            MessageBox mbox = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING | SWT.APPLICATION_MODAL);
            mbox.setText("Warning");
            mbox.setMessage("Save current changes?");
            if (mbox.open() == SWT.CANCEL) {
                isSaveChanges = false;
            }
        }
        if (isSaveChanges) {
            runSave();
        }
        String errorMsg = FileUtil.cp(currentFile, newFile, isOverride);
        if (errorMsg == null) {
            openFile(newFile.getAbsolutePath(), FileHandlerUtil.INVALID_LINE_NUMBER, false);
        } else {
            MessageBox mbox = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING | SWT.APPLICATION_MODAL);
            mbox.setText("Warning");
            mbox.setMessage("Unable to create a new file.\nReason: " + errorMsg);
        }
    }
