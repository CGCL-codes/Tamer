    private String export2file(String profileName) {
        FileDialog dialog = new FileDialog(PluginUtil.getShell(), SWT.SAVE | SWT.SYSTEM_MODAL);
        dialog.setText("Export Log Control Profile");
        dialog.setFilterExtensions(new String[] { "*" + ZIP_EXT });
        dialog.setFilterNames(new String[] { "Log Control Profile (*" + ZIP_EXT + ")" });
        if (!profileName.endsWith(ZIP_EXT)) profileName = profileName + ZIP_EXT;
        int index = profileName.indexOf(Constants.pathSeparator);
        while (index != -1) {
            profileName = profileName.substring(0, index) + "_" + profileName.substring(index + 1);
            index = profileName.indexOf(Constants.pathSeparator);
        }
        dialog.setFileName(profileName);
        if (lastFileDialogPath == null) dialog.setFilterPath(System.getProperty("user.home") + "\\Desktop"); else dialog.setFilterPath(lastFileDialogPath);
        final String name = dialog.open();
        if (name == null || name.compareTo("") == 0) {
            return "";
        }
        File file = new File(name);
        if (file.exists()) {
            MessageBox mbox = new MessageBox(parentShell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING | SWT.APPLICATION_MODAL);
            mbox.setText("Warning");
            mbox.setMessage("This file already exists. Do you want to overwrite it?");
            if (mbox.open() == SWT.CANCEL) return "";
        }
        lastFileDialogPath = dialog.getFilterPath();
        return name;
    }
