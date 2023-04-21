    public synchronized void disconnect() throws KnxIpException, InterruptedException, IOException {
        if (this.destDataEndpointAddr == null) throw new KnxIpException("Not connected");
        IpMessage resp = this.processor.service(new IpDisconnectReq(this.channelId, new Hpai(this.processor.getSrcAddr())), this.destDataEndpointAddr);
        if (resp instanceof IpDisconnectResp) {
            IpDisconnectResp cr = (IpDisconnectResp) resp;
            if (this.channelId == cr.getChannelId()) {
                Runtime.getRuntime().removeShutdownHook(IpTunnelClient.this.shutdownHook);
                this.heartBeat.cancel();
                this.processor.stop();
                this.destDataEndpointAddr = null;
                int st = cr.getStatus();
                if (st != IpDisconnectResp.OK) {
                    throw new KnxIpException("Response error : " + st);
                }
            } else {
                throw new KnxIpException("Disconnect failed, response wrong channel id");
            }
        } else {
            throw new KnxIpException("Disconnect failed, unexpected response");
        }
    }
