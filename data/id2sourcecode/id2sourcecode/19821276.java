        private void monitor() throws KnxIpException, InterruptedException, IOException {
            Hpai ep = new Hpai(IpTunnelClient.this.processor.getSrcAddr());
            IpMessage resp = IpTunnelClient.this.processor.service(new IpConnectionStateReq(IpTunnelClient.this.channelId, ep), IpTunnelClient.this.destControlEndpointAddr);
            if (resp instanceof IpConnectionStateResp) {
                IpConnectionStateResp cr = (IpConnectionStateResp) resp;
                int cId = cr.getChannelId();
                if (cId == IpTunnelClient.this.channelId) {
                    int st = cr.getStatus();
                    if (st != IpConnectResp.OK) {
                        throw new KnxIpException("Monitor failed, response error : " + st);
                    }
                } else {
                    throw new KnxIpException("Monitor failed, wrong channel id : " + cId);
                }
            } else {
                throw new KnxIpException("Monitor failed, unexepected response");
            }
        }
