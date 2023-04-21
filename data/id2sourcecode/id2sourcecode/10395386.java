    private int getDataHash(byte[] data) {
        synchronized (crc32) {
            crc32.update(data);
            int crc = (int) crc32.getValue();
            crc32.reset();
            return crc;
        }
    }
