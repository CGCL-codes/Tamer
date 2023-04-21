    public static int write(int ref_num, byte data_buffer[], int offset, int len) {
        return readwrite(0xCB0000, ref_num, data_buffer, offset, len);
    }
