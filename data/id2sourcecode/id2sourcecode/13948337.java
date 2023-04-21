    public static void doQueryPeer(SGSServer server, ClientSession session, QueryPeer peer) {
        try {
            String you = accFromSesName(session.getName());
            SGSClientData l = server.getClientData(you);
            QueryPeerDone qpd = new QueryPeerDone();
            if (Str.nostr(l.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class))) {
                qpd.result = Results.NOT_IN_A_CHANNEL;
            } else {
                ChannelManager man = AppContext.getChannelManager();
                Channel channel = man.getChannel(l.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class));
                Set<ClientSession> sessions = channel.getSessions();
                Vector<String> accounts = new Vector<String>();
                Vector<Integer> nids = new Vector<Integer>();
                Vector<String> roles = new Vector<String>();
                Vector<Boolean> talkings = new Vector<Boolean>();
                for (Iterator<ClientSession> itor = sessions.iterator(); itor.hasNext(); ) {
                    ClientSession cur = itor.next();
                    if (cur == session) {
                        continue;
                    }
                    String him = accFromSesName(cur.getName());
                    SGSClientData himl = server.getClientData(him);
                    accounts.add(himl.getAccount());
                    nids.add(himl.get(SGSClientData.NSERVER_ID, Integer.class));
                    roles.add(himl.get(SGSClientData.ROLE, String.class));
                    talkings.add(himl.get(SGSClientData.TALKING, Boolean.class));
                }
                qpd.accounts = new String[accounts.size()];
                qpd.nids = new int[nids.size()];
                qpd.roles = new String[roles.size()];
                qpd.talkings = new boolean[talkings.size()];
                accounts.toArray(qpd.accounts);
                roles.toArray(qpd.roles);
                for (int i = 0; i < qpd.nids.length; i++) {
                    qpd.nids[i] = nids.get(i);
                    qpd.talkings[i] = talkings.get(i);
                }
            }
            session.send(Converter.getInstance().convert(qpd));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
