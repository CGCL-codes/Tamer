    public void titleChanged(IRCPanel panel) {
        String name;
        name = panel.getChannelName();
        if (name == null) setTabName(panel, "none"); else setTabName(panel, name);
    }
