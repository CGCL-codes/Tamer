    public static int write(int ref_num, byte data_buffer[]) {
        return readwrite(0xCB0000, ref_num, data_buffer, 0, data_buffer.length);
    }
