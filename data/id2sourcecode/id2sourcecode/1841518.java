    public void discardReadedBytes() {
        if (read_index > 0) {
            if (readable()) {
                int tmp = readableBytes();
                System.arraycopy(buffer, read_index, buffer, 0, tmp);
                read_index = 0;
                write_index = tmp;
            } else {
                read_index = write_index = 0;
            }
        }
    }
