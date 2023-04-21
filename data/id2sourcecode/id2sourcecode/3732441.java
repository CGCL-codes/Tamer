    public synchronized void write(Packet packet) {
        Header header = packet.getHeader();
        int time = status.getChannelAbsoluteTime(header);
        write(header.getPacketType(), packet.getData(), time);
    }
