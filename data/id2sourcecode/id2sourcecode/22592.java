    public byte[] overwriteFuzz(int start, int end, String stuff) {
        byte[] overwriteFuzz = readedByte;
        byte s = getByte(stuff);
        for (int i = start; i < end; i++) {
            overwriteFuzz[i] = s;
        }
        return overwriteFuzz;
    }
