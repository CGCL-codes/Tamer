    public void testCloseProperly1() throws Exception {
        tfi = new TransferredFileImpl(new RandomAccessFile(file, "rw"), 10, file, "name", 0);
        ByteBuffer dst = ByteBuffer.allocate(10);
        tfi.getChannel().read(dst);
        tfi.close();
        file.delete();
        assertFalse(file.exists());
    }
