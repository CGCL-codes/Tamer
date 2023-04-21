    public void hookDispatch(Hooks.Events what, String source, String target, String args) {
        super.hookDispatch(this, what, source, target, args);
        switch(what) {
            case E_JOINCHAN:
                if (Configuration.chanservice != null) if (((SrvChannel) Configuration.getSvc().get(Configuration.chanservice)).getChannels().containsKey(source.toLowerCase())) {
                } else {
                    if (Generic.Channels.containsKey(source.toLowerCase())) {
                        try {
                            for (Map.Entry<Client, UserModes> e : Generic.Channels.get(source.toLowerCase()).clientmodes.entrySet()) {
                                Generic.curProtocol.outSETMODE(this, source, "-oaqh", e.getKey().uid);
                                if (e.getKey().modes.contains("r")) Generic.curProtocol.outSETMODE(this, source, "+v", e.getKey().uid);
                            }
                            Generic.curProtocol.outTOPIC(this, source, "This is an unregistered channel. Only registered users may chat. Please see the network help channel for more information.");
                            Generic.curProtocol.outSETMODE(this, source, "+Mstl", "10");
                            Generic.curProtocol.outPRVMSG(this, source, "This is an unregistered channel. Only registered users may chat. Please see the network help channel for more information.");
                        } catch (NullPointerException NPE) {
                            NPE.printStackTrace();
                        }
                    } else {
                    }
                }
                break;
            case E_SIGNON:
                if (source != null) Generic.curProtocol.outMODE(this, Generic.Users.get(source.toLowerCase()), "+R", "");
            default:
        }
    }
