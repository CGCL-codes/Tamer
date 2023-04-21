    public void filter() throws IOException {
        byte[] buffer = new byte[512];
        int bytes_read;
        for (; ; ) {
            bytes_read = in.read(buffer);
            if (bytes_read == -1) return;
            for (int i = 0; i < bytes_read; i++) {
                if ((buffer[i] >= 'a') && (buffer[i] <= 'z')) {
                    buffer[i] = (byte) ('a' + ((buffer[i] - 'a') + 13) % 26);
                }
                if ((buffer[i] >= 'A') && (buffer[i] <= 'Z')) {
                    buffer[i] = (byte) ('A' + ((buffer[i] - 'A') + 13) % 26);
                }
            }
            out.write(buffer, 0, bytes_read);
        }
    }
