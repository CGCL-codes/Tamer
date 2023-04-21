    public static void deleteSelectedDir(String dirtodie) {
        File timetodie = new File(dirtodie);
        if (!timetodie.isDirectory()) return;
        File[] dirListing = timetodie.listFiles();
        Shell shell = new Shell();
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.NO | SWT.YES);
        messageBox.setText("Delete Confirmation");
        messageBox.setMessage("Are you sure you want to delete \n" + timetodie.getName() + "\nand all of its files?");
        int response = messageBox.open();
        switch(response) {
            case SWT.YES:
                try {
                    for (int i = 0; i < dirListing.length; i++) {
                        if (dirListing[i].isFile()) dirListing[i].delete();
                    }
                    if (timetodie.isDirectory()) timetodie.delete();
                    StatusBoxUtils.mainStatusAdd(" Directory " + timetodie + " deleted", 0);
                    Tab4.redrawBackupTable();
                    Tab4.deleteBackup.setEnabled(false);
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
