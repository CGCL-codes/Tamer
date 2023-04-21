    public static int writeBlock(int unit_num, int block_num, byte data_buffer[], int offset) {
        return readwriteBlock(0x810000, unit_num, block_num, data_buffer, offset);
    }
