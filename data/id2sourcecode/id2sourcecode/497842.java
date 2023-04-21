    public LoginPanel(ChatApplet applet) {
        this();
        _applet = applet;
        for (int i = 0; i < _applet.getChannels().size(); i++) {
            _channelChoice.add((String) _applet.getChannels().elementAt(i));
        }
    }
