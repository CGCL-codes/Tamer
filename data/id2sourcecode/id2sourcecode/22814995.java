    public User(final String nick, final String name, final String realname, final String host, final Channel chan, final UserChannelPermission chanRole) {
        this.setNick(nick);
        this.setName(name);
        this.setRealname(realname);
        this.setHost(host);
        if (chan != null) {
            this.getChannels().put(chan, chanRole);
        }
    }
