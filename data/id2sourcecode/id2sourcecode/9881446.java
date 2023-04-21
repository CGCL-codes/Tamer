    @Override
    public void send(Message msg) throws ChannelNotConnectedException, ChannelClosedException {
        SimulatedChannel dstChannel = m_Creator.getChannel(msg.getDest());
        dstChannel.m_Receiver.receive(msg);
    }
