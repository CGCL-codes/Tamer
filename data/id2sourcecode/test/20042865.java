    public Channel(IRCConfiguration config, String name, IRCServer s) {
        super(config, s);
        _name = name;
        _topic = "";
        _mode = new ModeHandler(s.getChannelModes(), s.getNickModes());
        _listeners = new ListenerGroup();
        _nicks = new Hashtable();
        s.addReplyServerListener(this);
        if (_ircConfiguration.getASLMaster()) getIRCServer().execute("WHO " + _name);
        setInterpretor(new ChannelInterpretor(config));
    }
