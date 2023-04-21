    public void handle(INonBlockingConnection client, LeaveChannelMessage message) throws IOException {
        SocketServer ss = SocketServer.getInstance();
        final ClientHandle cs = ss.get(client);
        if (cs != null) {
            Log.info(cs.getEmail() + " left channel " + cs.getChannel());
            KillPeerMessage killPeer = new KillPeerMessage();
            killPeer.id = cs.getId();
            for (Integer id : cs.getIds()) {
                ClientHandle ses = ss.get(id);
                ses.getIds().remove(Integer.valueOf(cs.getId()));
                ss.send(ses.connection(), killPeer);
            }
            cs.getIds().clear();
            Log.info("kill peer msg sent to the peers of " + cs.getEmail());
            Persister.getInstance().execute(new MarkAsOffline(cs.getChannel(), cs.getEmail()));
            Log.info("mark " + cs.getEmail() + " as offline on persistent store");
            cs.setChannelId(0);
            cs.setChannel(null);
            int state = cs.getState();
            state = State.set(state, State.IN_A_CHANNEL, false);
            cs.setState(state);
        } else {
            Log.info("no client found to match " + client.getAttachment());
        }
        LeaveChannelResponse response = new LeaveChannelResponse();
        response.code = Code.OK;
        ss.send(client, response);
    }
