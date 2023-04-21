    public static int read(int ref_num, byte data_buffer[]) {
        return readwrite(0xCA0000, ref_num, data_buffer, 0, data_buffer.length);
    }
