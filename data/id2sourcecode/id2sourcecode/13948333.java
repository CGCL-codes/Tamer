    public static void doEnterChannel(SGSServer server, ClientSession session, EnterChannel channel) {
        try {
            EnterChannelDone ecd = new EnterChannelDone();
            String you = accFromSesName(session.getName());
            ecd.name = channel.name;
            ecd.result = Results.UNKNOWN;
            SGSClientData l = server.getClientData(you);
            SGSChannelData cl = server.getChannelData(channel.name);
            if (cl != null) {
                if (canEnter(cl, you)) {
                    if (authenticate(cl, channel.password)) {
                        if (cl.get(SGSChannelData.MODERATOR, String.class).equals(you)) {
                            ecd.role = "moderator";
                            l.set(SGSClientData.ROLE, "moderator");
                        } else if (containsTeacher(cl, you)) {
                            ecd.role = "teacher";
                            l.set(SGSClientData.ROLE, "teacher");
                        } else {
                            ecd.role = "student";
                            l.set(SGSClientData.ROLE, "student");
                        }
                        join(server, cl, you);
                        l.set(SGSClientData.CURRENT_CHANNEL_NAME, channel.name);
                        l.set(SGSClientData.NSERVER_CHANNEL_ID, Integer.valueOf(cl.get(SGSChannelData.NSERVER_CHANNEL_ID, Integer.class)));
                        ecd.result = Results.OK;
                        ecd.cnid = cl.get(SGSChannelData.NSERVER_CHANNEL_ID, Integer.class);
                        NNewPeer nnp = new NNewPeer();
                        nnp.nid = l.get(SGSClientData.NSERVER_ID, Integer.class);
                        nnp.cnid = cl.get(SGSChannelData.NSERVER_CHANNEL_ID, Integer.class);
                        server.ndispatch(nnp);
                        NewPeer np = new NewPeer();
                        np.account = l.getAccount();
                        np.nid = l.get(SGSClientData.NSERVER_ID, Integer.class);
                        np.role = l.get(SGSClientData.ROLE, String.class);
                        channelwideDispatch(np, session, cl.getChannel());
                    } else {
                        ecd.result = Results.AUTH_FAIL;
                    }
                } else {
                    ecd.result = Results.NOT_ALLOWED;
                }
            } else {
                ecd.result = Results.NOT_FOUND;
            }
            session.send(Converter.getInstance().convert(ecd));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
