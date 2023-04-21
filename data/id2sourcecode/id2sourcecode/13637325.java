    public void perform() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        this.getChannel().read(buffer);
        buffer.flip();
        this.serverInitializationPacket = new SSHPacketKeyExchangeInitialization(buffer);
        System.out.println(this.getServerInitializationPacket());
        this.guessConfiguration();
        this.createClientInitializationPacket();
        this.getClient().getTransportManager().send(this.getClientInitializationPacket());
        buffer = ByteBuffer.allocateDirect(1024);
        int size = this.getChannel().read(buffer);
        buffer.flip();
        System.out.println(size);
    }
