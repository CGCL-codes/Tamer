    public void writeThreadId(ThreadId threadId) {
        byte[] data = threadId.getBytes();
        boolean[] found = new boolean[1];
        int index = threadIdCache.add(found, data);
        if (found[0]) {
            writeCompressedNumber(0);
        } else {
            writeCompressedNumber(data.length);
            writeBytes(data);
        }
        write16Bit(index);
    }
