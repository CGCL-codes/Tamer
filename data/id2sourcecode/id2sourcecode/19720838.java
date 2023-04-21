    public void send(ByteBuffer buffer) {
        try {
            if (ready) channel.write(buffer);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
