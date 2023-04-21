    public void open() throws IOException {
        SSHPacketChannelOpen packet = new SSHPacketChannelOpen();
        packet.type = this.getType();
        this.getClient().getTransportManager().send(packet);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        int size = this.getChannel().read(buffer);
        buffer.flip();
        System.out.println(size);
    }
