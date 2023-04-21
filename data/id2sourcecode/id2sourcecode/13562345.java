    public void setLength(long length) {
        if (write) throw new IndexOutOfBoundsException("Cannot set length for the HTTP output stream. Bytes are already written to this stream.");
        this.length = length;
    }
