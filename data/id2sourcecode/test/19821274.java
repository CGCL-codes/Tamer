    @Override
    public void notifyMessage(IpMessage message) {
        if (message instanceof IpTunnelingReq) {
            IpTunnelingReq req = (IpTunnelingReq) message;
            if (req.getChannelId() == this.channelId) {
                int seqCounter = req.getSeqCounter();
                try {
                    this.processor.send(new IpTunnelingAck(this.channelId, seqCounter, IpTunnelingAck.OK), this.destDataEndpointAddr);
                } catch (IOException e) {
                }
                IpMessageListener l = this.messageListener;
                if (l != null) {
                    l.receive(req.getcEmiFrame());
                }
            } else {
            }
        }
    }
