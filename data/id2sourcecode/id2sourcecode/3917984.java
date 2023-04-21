    public void run(String filename, boolean forceBackupDelete) {
        File file = new File(filename);
        if (file.exists()) {
            boolean canRun = true;
            ExtFile backupTest = new ExtFile(file.getAbsolutePath() + ".bak");
            if (backupTest.exists()) {
                if (forceBackupDelete) {
                    backupTest.delete();
                } else {
                    canRun = false;
                    System.out.println("A backup file with the name ");
                    System.out.println("'" + backupTest.getAbsolutePath() + "'");
                    System.out.println("already exists.");
                    System.out.println("Remove this file before calling 'Defragment'.");
                }
            }
            if (canRun) {
                file.renameTo(backupTest);
                try {
                    configureDb4o();
                    ObjectContainer readFrom = Db4o.openFile(backupTest.getAbsolutePath());
                    ObjectContainer writeTo = Db4o.openFile(file.getAbsolutePath());
                    writeTo.ext().migrateFrom(readFrom);
                    migrate(readFrom, writeTo);
                    readFrom.close();
                    writeTo.close();
                    System.out.println("Defragment operation completed successfully.");
                } catch (Exception e) {
                    System.out.println("Defragment operation failed.");
                    e.printStackTrace();
                    try {
                        new File(filename).delete();
                        backupTest.copy(filename);
                    } catch (Exception ex) {
                        System.out.println("Restore failed.");
                        System.out.println("Please use the backup file:");
                        System.out.println("'" + backupTest.getAbsolutePath() + "'");
                        return;
                    }
                    System.out.println("The original file was restored.");
                    try {
                        new File(backupTest.getAbsolutePath()).delete();
                    } catch (Exception ex) {
                    }
                } finally {
                    restoreConfiguration();
                }
            }
        } else {
            System.out.println("File '" + file.getAbsolutePath() + "' does not exist.");
        }
    }
