            public void actionPerformed(ActionEvent e) {
                if (lock != null) {
                    try {
                        lock.release();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    RandomAccessFile raf = null;
                    try {
                        raf = new RandomAccessFile(PATH, "rw");
                        lock = raf.getChannel().lock();
                        System.err.println("[LockSample] " + lock);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                boolean canLock = updateLockState();
            }
