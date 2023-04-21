    private static byte[] getBytesFromHash(byte[] dataToHash) {
        byte[] result = null;
        synchronized (data) {
            data.update(dataToHash, 0, dataToHash.length);
            result = data.digest();
        }
        return result;
    }
