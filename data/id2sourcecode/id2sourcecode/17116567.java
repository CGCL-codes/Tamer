    public byte[] hash(String str) {
        opLock.lock();
        try {
            byte[] bt = mHasher.digest(str.getBytes());
            if (null == bt || bt.length != 16) {
                throw new IllegalArgumentException("md5 need");
            }
            return bt;
        } finally {
            opLock.unlock();
        }
    }
