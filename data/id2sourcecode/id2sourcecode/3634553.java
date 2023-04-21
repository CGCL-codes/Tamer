    public void run() {
        packetwriter = new XMPPPacketWriter(writer, this);
        Thread readerthread = new Thread(new XMPPPacketReader(reader, packetwriter));
        readerthread.start();
    }
