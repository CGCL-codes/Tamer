    public void socketReadyForRead() {
        if (!isOpen()) return;
        try {
            if (!isConnected()) throw new IOException("Channel not connected.");
            while (m_socketReader.read(getChannel()) > 0) {
                byte[] packet;
                ByteBuffer buffer = m_socketReader.getBuffer();
                while (buffer.remaining() > 0 && (packet = m_packetReader.nextPacket(buffer)) != null) {
                    if (packet == PacketReader.SKIP_PACKET) continue;
                    notifyPacketReceived(packet);
                }
                m_socketReader.compact();
            }
        } catch (Exception e) {
            close(e);
        }
    }
