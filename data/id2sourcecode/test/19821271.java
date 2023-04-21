    public synchronized void service(byte[] message) throws KnxIpException, InterruptedException, IOException {
        if (this.destDataEndpointAddr == null) this.connect();
        IpMessage resp = this.processor.service(new IpTunnelingReq(this.channelId, this.seqCounter, message), this.destDataEndpointAddr);
        if (resp == null) {
            throw new KnxIpException("No response");
        } else {
            if (resp instanceof IpTunnelingAck) {
                IpTunnelingAck cr = (IpTunnelingAck) resp;
                if (cr.getChannelId() == this.channelId) {
                    if (cr.getSeqCounter() == this.seqCounter) {
                        int st = cr.getStatus();
                        if (st != IpTunnelingAck.OK) {
                            throw new KnxIpException("Response error : " + st);
                        }
                    } else {
                        this.disconnect();
                        throw new KnxIpException("Tunnel failed, response wrong sequence counter value, expected " + this.seqCounter + ", got " + cr.getSeqCounter());
                    }
                } else {
                    this.disconnect();
                    throw new KnxIpException("Tunnel failed, response wrong channel id");
                }
            }
        }
        this.seqCounter = (this.seqCounter + 1 & 0xFF);
    }
