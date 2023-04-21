    public synchronized void connect() throws KnxIpException, InterruptedException, IOException {
        if (this.destDataEndpointAddr != null) throw new KnxIpException("Already connected");
        this.processor.start();
        Hpai ep = new Hpai(this.processor.getSrcAddr());
        IpMessage resp = this.processor.service(new IpConnectReq(ep, ep), this.destControlEndpointAddr);
        if (resp instanceof IpConnectResp) {
            IpConnectResp cr = (IpConnectResp) resp;
            int st = cr.getStatus();
            if (st == IpConnectResp.OK) {
                this.channelId = cr.getChannelId();
                this.destDataEndpointAddr = cr.getDataEndpoint().getAddress();
                this.heartBeat = new Timer("KNX IP heartbeat");
                this.heartBeat.schedule(new HeartBeatTask(), 0, 60000);
                Runtime.getRuntime().addShutdownHook(IpTunnelClient.this.shutdownHook);
            } else {
                throw new KnxIpException("Connect failed, response error : " + st);
            }
        } else {
            throw new KnxIpException("Connect failed, unexpected response");
        }
    }
