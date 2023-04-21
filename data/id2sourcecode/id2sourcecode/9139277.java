    public synchronized SSH2SessionChannel newTerminal(SSH2TerminalAdapter termAdapter) {
        SSH2SessionChannel channel = new SSH2SessionChannel(this);
        SSH2TransportPDU pdu = getChannelOpenPDU(channel);
        termAdapter.attach(channel);
        transmit(pdu);
        return channel;
    }
