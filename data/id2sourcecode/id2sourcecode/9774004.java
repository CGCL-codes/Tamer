    public void testCanReadInt() throws IOException {
        Output write = new Output(new ByteArrayOutputStream());
        Input read = new Input(write.toBytes());
        assertEquals(false, read.canReadInt());
        write.writeInt(400, true);
        read = new Input(write.toBytes());
        assertEquals(true, read.canReadInt());
        read.setLimit(read.limit() - 1);
        assertEquals(false, read.canReadInt());
    }
