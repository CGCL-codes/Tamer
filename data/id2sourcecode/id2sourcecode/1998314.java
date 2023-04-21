    public void readMessage(ChannelCallback callback) throws Exception, InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFSIZE);
        int num = callback.getChannel().read(byteBuffer);
        byteBuffer.flip();
        byte[] result = new byte[num];
        for (int i = 0; i < num; i++) {
            result[i] = byteBuffer.array()[i];
        }
        callback.append(result);
    }
