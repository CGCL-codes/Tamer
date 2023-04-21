    public void readMessage(DataCallback callback) throws IOException, InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFSIZE);
        callback.getChannel().read(byteBuffer);
        byteBuffer.flip();
        String result = decode(byteBuffer);
        callback.record(result.toString());
        if (result.indexOf(WRITE_FILE) >= 0) {
            callback.execute();
            callback.getChannel().close();
            System.out.println("channel closed");
        }
    }
