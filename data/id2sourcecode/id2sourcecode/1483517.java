    private void testReadWriteImpl(FileChannel channel, final int COUNT) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        for (int i = COUNT; i-- > 0; ) {
            buffer.clear();
            buffer.putInt(i).flip();
            channelUtil.writeRemaining(channel, buffer);
        }
        assertEquals(COUNT * 4, channel.position());
        assertEquals(COUNT * 4, channel.size());
        checkReadWriteTestContents(channel, COUNT);
        ByteBuffer[] buffers = new ByteBuffer[COUNT];
        for (int i = COUNT; i-- > 0; ) buffers[i] = ByteBuffer.allocate(4);
        channel.position(0);
        channelUtil.readRemaining(channel, buffers);
        for (int i = 0, count = COUNT; count-- > 0; ++i) {
            buffers[i].rewind();
            assertEquals(count, buffers[i].getInt());
        }
        File workFile = env.getTestCaseFile(this);
        FileChannel sink = new RandomAccessFile(workFile, "rw").getChannel();
        channelUtil.transferTo(channel, 0, channel.position(), sink);
        checkReadWriteTestContents(sink, COUNT);
        {
            buffer.clear();
            buffer.putInt(0);
            buffer.flip();
            channel.position(0);
            for (int i = COUNT; i-- > 0; ) {
                buffer.rewind();
                channelUtil.writeRemaining(channel, buffer);
            }
            channel.truncate(0);
            assertEquals(0, channel.size());
        }
        channelUtil.transferFrom(channel, 0, sink.size(), sink.position(0));
        checkReadWriteTestContents(channel, COUNT);
    }
