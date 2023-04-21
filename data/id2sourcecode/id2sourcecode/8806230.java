    public synchronized boolean obtain() throws IOException {
        if (isLocked()) {
            return false;
        }
        if (!lockDir.exists()) {
            if (!lockDir.mkdirs()) throw new IOException("Cannot create directory: " + lockDir.getAbsolutePath());
        } else if (!lockDir.isDirectory()) {
            throw new IOException("Found regular file where directory expected: " + lockDir.getAbsolutePath());
        }
        String canonicalPath = path.getCanonicalPath();
        boolean markedHeld = false;
        try {
            synchronized (LOCK_HELD) {
                if (LOCK_HELD.contains(canonicalPath)) {
                    return false;
                } else {
                    LOCK_HELD.add(canonicalPath);
                    markedHeld = true;
                }
            }
            try {
                f = new RandomAccessFile(path, "rw");
            } catch (IOException e) {
                failureReason = e;
                f = null;
            }
            if (f != null) {
                try {
                    channel = f.getChannel();
                    try {
                        lock = channel.tryLock();
                    } catch (IOException e) {
                        failureReason = e;
                    } finally {
                        if (lock == null) {
                            try {
                                channel.close();
                            } finally {
                                channel = null;
                            }
                        }
                    }
                } finally {
                    if (channel == null) {
                        try {
                            f.close();
                        } finally {
                            f = null;
                        }
                    }
                }
            }
        } finally {
            if (markedHeld && !isLocked()) {
                synchronized (LOCK_HELD) {
                    if (LOCK_HELD.contains(canonicalPath)) {
                        LOCK_HELD.remove(canonicalPath);
                    }
                }
            }
        }
        return isLocked();
    }
