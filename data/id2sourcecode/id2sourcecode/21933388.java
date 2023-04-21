    public void testCloseProperly2() throws Exception {
        tfi = new TransferredFileImpl(file, "name", "rw");
        ByteBuffer dst = ByteBuffer.allocate(10);
        tfi.getChannel().read(dst);
        tfi.close();
        file.delete();
        assertFalse(file.exists());
    }
