    protected void handleSelectionKeyEvent(SocketEvent event) {
        SocketChannel channel = (SocketChannel) event.getSocket().getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            channel.read(buffer);
            buffer.flip();
            Charset charset = Charset.forName("ISO-8859-1");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            System.out.print(charBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
