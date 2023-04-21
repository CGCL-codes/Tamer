    public void reload() throws IOException {
        FileChannel c = null;
        try {
            c = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer b = ByteBuffer.allocate(8);
            c.position(SERIAL_OFFSET);
            c.read(b);
            b.flip();
            serialNumber = b.getLong();
            loadData(c);
            saveLastKnownGood(c);
        } finally {
            if (c != null) c.close();
        }
    }
