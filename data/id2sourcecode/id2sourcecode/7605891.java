            @Override
            public void run() {
                synchronized (TestFileManagementOptions.this) {
                    TestFileManagementOptions.this.notifyAll();
                    final File file = new File(path, "store.data");
                    RandomAccessFile randomAccessFile;
                    try {
                        randomAccessFile = new RandomAccessFile(file, "rws");
                        FileLock lock = null;
                        try {
                            do {
                                try {
                                    lock = randomAccessFile.getChannel().tryLock();
                                } catch (final Exception e) {
                                    lock = null;
                                }
                                if (lock == null && !stop) {
                                    TestFileManagementOptions.this.wait(0);
                                }
                            } while (!stop && lock == null);
                            if (!stop) {
                                TestFileManagementOptions.this.wait();
                            }
                        } catch (final Exception e) {
                        }
                        try {
                            lock.release();
                        } catch (final Exception e) {
                        }
                        try {
                            randomAccessFile.close();
                        } catch (final Exception e) {
                        }
                    } catch (final Exception e1) {
                    }
                    TestFileManagementOptions.this.notifyAll();
                }
            }
