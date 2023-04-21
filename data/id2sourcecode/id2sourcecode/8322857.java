        private void makeCopyOfBackup(BackupTask backupTask, File dest) throws Exception {
            backupTask.join();
            File backupFile = backupTask.getBackupFile();
            if (backupFile == null) throw new Exception("Couldn't backup data");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            if (dest.getName().toLowerCase().endsWith(PDBK)) out = new XorOutputStream(out, CompressedInstanceLauncher.PDASH_BACKUP_XOR_BITS);
            FileUtils.copyFile(backupFile, out);
            out.close();
        }
