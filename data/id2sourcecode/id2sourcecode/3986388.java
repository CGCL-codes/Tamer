    public void writeBuffer(Buffer b) {
        int l = b.writePos - b.readPos;
        willWrite(l);
        System.arraycopy(b.data, b.readPos, data, writePos, l);
        wrote(l);
    }
