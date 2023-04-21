    public static int readwriteBlock(int cmd, int unit_num, int block_num, byte data_buffer[], int offset) {
        byte params[] = new byte[6];
        if ((offset + 512) < data_buffer.length) return -256;
        int hmem = vm02.refAsBits((Object) data_buffer) & 0xFFFF;
        int bufferptr = vm02.call(hmem, 0x0E) + 2 + offset;
        params[0] = 3;
        params[1] = (byte) unit_num;
        params[2] = (byte) bufferptr;
        params[3] = (byte) (bufferptr >> 8);
        params[4] = (byte) block_num;
        params[5] = (byte) (block_num >> 8);
        int result = vm02.call(cmd | (vm02.refAsBits((Object) params) & 0xFFFF), 0x54) & 0xFF;
        vm02.call(hmem, 0x10);
        return -result;
    }
