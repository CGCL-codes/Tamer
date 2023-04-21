    public String saveFileWorkspace(Shell shell, boolean forceSave) {
        if (!forceSave || currentWorkspaceFileNameShort == null) {
            FileDialog dialog = new FileDialog(PluginUtil.getShell(), SWT.SAVE | SWT.SYSTEM_MODAL);
            dialog.setText("Save As...");
            dialog.setFilterExtensions(new String[] { "*" + ZIP_EXT });
            dialog.setFilterNames(new String[] { "PLATO Workspace Files (*" + ZIP_EXT + ")" });
            if (currentWorkspaceFileNameShort == null) dialog.setFileName(DEFAULT_NAME + ZIP_EXT); else dialog.setFileName(currentWorkspaceFileNameShort);
            if (lastFileDialogPath == null) dialog.setFilterPath(System.getProperty("user.home") + SEP + "Desktop"); else dialog.setFilterPath(lastFileDialogPath);
            final String name = dialog.open();
            if (name == null || name.compareTo("") == 0) {
                return null;
            }
            File file = new File(name);
            if (file.exists()) {
                MessageBox mbox = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING | SWT.APPLICATION_MODAL);
                mbox.setText("Warning");
                mbox.setMessage("This file already exists. Do you want to overwrite it?");
                if (mbox.open() == SWT.CANCEL) return null;
            }
            currentWorkspaceFileNameShort = dialog.getFileName();
            updateTitleBar(null, null);
            lastFileDialogPath = dialog.getFilterPath();
            currentWorkspaceFileNameFull = name;
        }
        saveFileWorkspace(currentWorkspaceFileNameFull);
        projectModified = false;
        updateTitleBar(null, null);
        return currentWorkspaceFileNameFull;
    }
