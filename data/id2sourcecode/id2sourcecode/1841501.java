    public int readableBytes() {
        return readable() ? write_index - read_index : 0;
    }
