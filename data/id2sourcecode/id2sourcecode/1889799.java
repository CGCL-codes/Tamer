    public static void redrawBackupTable() {
        ButtonStatus.set(true, true, false, true, true);
        int j = 0;
        try {
            final String backupinstalldirectory = DirectoryUtils.getBackupDirectory();
            final String backupDirectory = backupinstalldirectory + System.getProperty("file.separator") + "config";
            File f = new File(backupDirectory);
            if (f.isFile()) {
                f.delete();
            }
            if (!f.isDirectory()) {
                f.mkdir();
            }
            File[] files = f.listFiles();
            backupFiles = new File[files.length];
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    backupFiles[j] = files[i];
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int tableLength = j;
        View.getDisplay().syncExec(new Runnable() {

            public void run() {
                if (backupTable.isDisposed()) return;
                try {
                    backupTable.setItemCount(tableLength);
                } catch (Exception e) {
                    backupTable.setItemCount(0);
                }
                backupTable.clearAll();
            }
        });
    }
