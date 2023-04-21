    public static byte[] swapByteOrder32(byte[] data, int ofs, int len) {
        int end = ofs + len;
        byte tmp;
        for (; ofs < end; ofs += 4) {
            tmp = data[ofs];
            data[ofs] = data[ofs + 3];
            data[ofs + 3] = tmp;
            tmp = data[ofs + 1];
            data[ofs + 1] = data[ofs + 2];
            data[ofs + 2] = tmp;
        }
        return data;
    }
