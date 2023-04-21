    public void connectLocalChannel(SSH2Channel channel, String remoteAddr, int remotePort, String originAddr, int originPort) {
        SSH2TransportPDU pdu = getChannelOpenPDU(channel);
        pdu.writeString(remoteAddr);
        pdu.writeInt(remotePort);
        pdu.writeString(originAddr);
        pdu.writeInt(originPort);
        transmit(pdu);
    }
