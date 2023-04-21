    public void initialize(ApplicationManager am) {
        if (RecordStoreImpl.rmsDir != null) {
            return;
        }
        if (am.applet != null) {
            RecordStoreImpl.rmsDir = null;
        } else {
            RecordStoreImpl.rmsDir = new File(am.getProperty("rms.home", ".rms"));
            boolean hasRms;
            try {
                hasRms = RecordStoreImpl.rmsDir.exists();
            } catch (Throwable th) {
                hasRms = false;
            }
            if (hasRms) {
                try {
                    final File lockFile = new File(RecordStoreImpl.rmsDir, "in.use");
                    if (lockFile.exists()) {
                        System.err.println("[RecordStoreInitializer] in.use file found! " + lockFile);
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
                            int result = JOptionPane.showConfirmDialog(null, "ME4SE may already be running, are you sure you want to run anyway?", "Question?", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.NO_OPTION) {
                                System.exit(0);
                            }
                        }
                        lockFile.delete();
                    }
                    FileOutputStream lockFileOS = new FileOutputStream(lockFile);
                    lockFileOS.close();
                    final FileChannel lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
                    final FileLock lock = lockChannel.tryLock();
                    if (lock == null) throw new Exception("Unable to obtain lock");
                    Runtime.getRuntime().addShutdownHook(new Thread() {

                        public void run() {
                            try {
                                lock.release();
                                lockChannel.close();
                                lockFile.delete();
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An instance of ME4SE is already running.", "Warning", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
                String[] files = RecordStoreImpl.rmsDir.list();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        String fileName = files[i];
                        if (fileName.endsWith(".rms")) {
                            String name = RecordStoreImpl.decode(fileName.substring(0, fileName.length() - 4));
                            AbstractRecordStore.recordStores.put(name, new RecordStoreImpl());
                        }
                    }
                }
            }
        }
    }
