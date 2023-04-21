    public void handle(NetworkManager man, DatagramSession server, Packet packet) throws Exception {
        if (getStore().getChannel() != null) return;
        EChnlRsp f = new EChnlRsp();
        f.deserialize(packet.getBuf(), Protocol.RUDP_CURSOR);
        ChannelBean cbean = f.getChannelBean();
        if (f.getStatusCode() == StatusCode.OK) getStore().setChannel(cbean);
        ChannelBean[] array = new ChannelBean[] { cbean };
        ChannelEvent e = new ChannelEvent(this, array, f.getStatusCode());
        man.fireOnEnteredChannel(e);
        PeerBean[] pbeans = f.getPeerBeans();
        man.fireOnFoundPeers(new PeerEvent(this, pbeans));
        if (pbeans == null) return;
        for (PeerBean pbean : pbeans) {
            Speaker speaker = new Speaker();
            pbean.setData(speaker);
            pbean.fireOnTalkingChanged(new com.peterhi.beans.PeerEvent(this));
            if (pbean.getTalkState() == TalkState.On) speaker.start();
            int id = getStore().getID();
            int tid = pbean.getID();
            DatagramSession ses = new DatagramSession(man, false);
            ses.setTsa(server.getRemoteSocketAddress());
            ses.start();
            man.putPeerSession(tid, ses);
            ISesMsg m = new ISesMsg();
            m.setClientHashCode(id);
            m.setTargetClientHashCode(tid);
            man.udpPost(tid, m);
        }
    }
