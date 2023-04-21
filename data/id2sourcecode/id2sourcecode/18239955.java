    public static boolean tryDirectory(String subdirName) {
        boolean permission = false;
        String possibleHomeDir = ConfigureTranche.get(ConfigureTranche.CATEGORY_SERVER, ConfigureTranche.PROP_SERVER_DIRECTORY);
        try {
            String userHome = System.getProperty("user.home");
            if (userHome != null) {
                possibleHomeDir = System.getProperty("user.home") + File.separator + subdirName;
            }
        } catch (Exception ex) {
            System.err.println("Exception occurred while checking system properties: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
        File testDir = new File(possibleHomeDir, "test_permissions");
        File lockFile = null;
        RandomAccessFile lockRAF = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {
            lockFile = new File(possibleHomeDir, "ffts-permissions-test.lock");
            if (!lockFile.exists()) {
                lockFile.getParentFile().mkdirs();
                lockFile.createNewFile();
            }
            lockRAF = new RandomAccessFile(lockFile, "rw");
            boolean obtainedLock = false;
            int attempt = 0;
            channel = lockRAF.getChannel();
            while (!obtainedLock && attempt < 50) {
                try {
                    lock = channel.lock();
                    obtainedLock = true;
                } catch (Exception ex) {
                    Thread.sleep(10);
                } finally {
                    attempt++;
                }
            }
            IOUtil.recursiveDelete(testDir);
            boolean success = testDir.mkdirs();
            permission = success && testDir.exists() && testDir.canRead() && testDir.canWrite();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            IOUtil.recursiveDelete(testDir);
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (lockRAF != null) {
                try {
                    lockRAF.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            IOUtil.safeDelete(lockFile);
        }
        if (permission) {
            FlatFileTrancheServer.defaultHomeDirectory = possibleHomeDir;
            return true;
        } else {
            System.err.println("Could not create persistent directory at " + possibleHomeDir);
        }
        return false;
    }
