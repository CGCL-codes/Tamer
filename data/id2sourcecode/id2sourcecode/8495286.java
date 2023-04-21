    public void readMessage(ChannelCallback callback) throws IOException, InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFSIZE);
        int nbytes = callback.getChannel().read(byteBuffer);
        byteBuffer.flip();
        String result = this.decode(byteBuffer);
        log.debug(result);
        if (result.indexOf("quit") >= 0) callback.getChannel().close(); else if (result.indexOf("shutdown") >= 0) {
            callback.getChannel().close();
            throw new InterruptedException();
        } else {
            callback.append(result.toString());
            if (result.indexOf("\n") >= 0) callback.execute();
        }
    }
