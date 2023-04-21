    public static String getStringFromHash(byte[] dataToHash) {
        String result = null;
        byte[] byteResult = null;
        synchronized (data) {
            data.update(dataToHash, 0, dataToHash.length);
            byteResult = data.digest();
        }
        result = hexStringFromByteArray(byteResult);
        return result;
    }
