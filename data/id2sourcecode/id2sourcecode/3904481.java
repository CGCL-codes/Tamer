    @Override
    public void onRead() throws IOException {
        ByteBuffer input = ByteBuffer.allocate(10000);
        try {
            ((ReadableByteChannel) this.channel).read(input);
        } catch (IOException e) {
            register(this.registeredOperations - (SelectionKey.OP_READ & this.registeredOperations));
            return;
        }
        input.flip();
        if (input.position() == input.limit()) {
            onClientDisconnected();
            return;
        }
        if (input.position() == input.limit()) {
            register(this.registeredOperations - (SelectionKey.OP_READ & this.registeredOperations));
        }
        this.toThread.write(input.array(), input.position(), input.limit());
        input.clear();
    }
