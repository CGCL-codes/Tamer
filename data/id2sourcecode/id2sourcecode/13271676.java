    public void perform() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        this.getChannel().read(buffer);
        buffer.flip();
        String serverVersion = this.getClient().getDecoder().decode(buffer).toString();
        System.out.println(serverVersion);
        Packet packet = new Packet(1024);
        packet.put((SSHClient.ID + "\r\n").getBytes());
        this.getClient().getTransportManager().send(packet);
    }
