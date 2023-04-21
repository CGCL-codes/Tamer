    public static int readwrite(int cmd, int ref_num, byte data_buffer[], int offset, int len) {
        byte params[] = new byte[8];
        if ((offset + len) > data_buffer.length) return -256;
        int hmem = vm02.refAsBits((Object) data_buffer) & 0xFFFF;
        int bufferptr = (vm02.call(hmem, 0x0E) & 0xFFFF) + 2 + offset;
        params[0] = 4;
        params[1] = (byte) ref_num;
        params[2] = (byte) bufferptr;
        params[3] = (byte) (bufferptr >> 8);
        params[4] = (byte) len;
        params[5] = (byte) (len >> 8);
        int result = vm02.call(cmd | (vm02.refAsBits((Object) params) & 0xFFFF), 0x54) & 0xFF;
        vm02.call(hmem, 0x10);
        return (result != 0) ? -result : (((int) params[6] & 0x00FF) | (((int) params[7] << 8) & 0xFF00));
    }
