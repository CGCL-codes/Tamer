    public static void deleteMultiDirs(String[] dirs_to_die) {
        String file_names = "";
        File[] dirFiles = new File[dirs_to_die.length];
        for (int i = 0; i < dirs_to_die.length; i++) {
            File isDir = new File(dirs_to_die[i]);
            if (!isDir.isDirectory()) return;
            file_names = file_names + "\n" + isDir.getName();
        }
        Shell shell = new Shell();
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.NO | SWT.YES);
        messageBox.setText("Delete Confirmation");
        messageBox.setMessage("Are you sure you want to delete the following directories?" + file_names + "\nand all of their configuration files?");
        int response = messageBox.open();
        switch(response) {
            case SWT.YES:
                try {
                    for (int j = 0; j < dirs_to_die.length; j++) {
                        dirFiles[j] = new File(dirs_to_die[j]);
                        File[] dirListing = dirFiles[j].listFiles();
                        for (int i = 0; i < dirListing.length; i++) {
                            if (dirListing[i].isFile()) dirListing[i].delete();
                        }
                        dirFiles[j].delete();
                        StatusBoxUtils.mainStatusAdd(" Directory " + dirFiles[j].getPath() + " deleted", 0);
                    }
                    Tab4.redrawBackupTable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (View.getDisplay() == null || View.getDisplay().isDisposed()) return;
                View.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        if (Tab4.backupTable != null && !Tab4.backupTable.isDisposed()) {
                            Tab4.backupTable.deselectAll();
                        }
                    }
                });
                shell.dispose();
                break;
            case SWT.NO:
                if (View.getDisplay() == null || View.getDisplay().isDisposed()) return;
                View.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        StatusBoxUtils.mainStatusAdd(" Directory Delete Cancelled", 0);
                        if (Tab4.backupTable != null && !Tab4.backupTable.isDisposed()) {
                            Tab4.backupTable.deselectAll();
                        }
                        if (Tab4.deleteBackup != null && !Tab4.deleteBackup.isDisposed()) {
                            Tab4.deleteBackup.setEnabled(false);
                        }
                    }
                });
                shell.dispose();
                break;
        }
    }
