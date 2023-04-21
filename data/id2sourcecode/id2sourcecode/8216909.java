    public void handle(NetworkManager man, DatagramSession session, byte[] data) {
        try {
            EChnlRsp f = new EChnlRsp();
            f.deserialize(data, 22);
            if (f.getStatusCode() == StatusCode.OK) getStore().setChannel(f.getChannelBean());
            ChannelBean[] array = new ChannelBean[] { f.getChannelBean() };
            ChannelEvent e = new ChannelEvent(this, array, f.getStatusCode());
            man.fireOnEnteredChannel(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
