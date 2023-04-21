    public byte[] digest() {
        byte[] result = new byte[UMac32.OUTPUT_LEN];
        for (int i = 0; i < streams; i++) {
            byte[] partialResult = l1hash[i].digest();
            System.arraycopy(partialResult, 0, result, 4 * i, 4);
        }
        reset();
        return result;
    }
