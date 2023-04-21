    public byte[] digest() {
        byte[] result = uhash32.digest();
        byte[] pad = pdf();
        for (int i = 0; i < OUTPUT_LEN; i++) result[i] = (byte) (result[i] ^ pad[i]);
        return result;
    }
