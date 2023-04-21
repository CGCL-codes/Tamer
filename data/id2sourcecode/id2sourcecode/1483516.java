    private void testTransferFromBoundaryConditionImpl(FileChannel channel) throws Exception {
        channel.truncate(0);
        ByteBuffer buffer = ByteBuffer.allocate(16);
        for (int i = 16; i-- > 0; ) buffer.put((byte) i);
        buffer.flip();
        channelUtil.writeRemaining(channel, buffer);
        assertEquals(buffer.limit(), channel.size());
        assertEquals(buffer.limit(), channel.position());
        FileChannel arg = new RandomAccessFile(env.getTestCaseFile(this), "rw").getChannel();
        buffer.rewind().limit(8);
        channelUtil.writeRemaining(arg, buffer);
        arg.position(0);
        long amountWritten = channel.transferFrom(arg, channel.size() - 3, 4);
        assertTrue(amountWritten <= 4);
        assertTrue(amountWritten >= 0);
        amountWritten = channel.transferFrom(arg, channel.size() + 1, 4);
        assertEquals(0, amountWritten);
    }
