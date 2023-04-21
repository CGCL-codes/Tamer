    protected void enlarge(int minimum) {
        int newSize = Math.max(data.length, MINSIZE);
        while (newSize < remaining() + minimum) newSize <<= 1;
        byte[] newData = new byte[newSize];
        System.arraycopy(data, readPos, newData, 0, writePos - readPos);
        writePos -= readPos;
        readPos = 0;
        data = newData;
    }
