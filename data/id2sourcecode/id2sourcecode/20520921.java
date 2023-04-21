    public User(User user) {
        this.nick = user.getNick();
        this.visibleNick = user.getVisibleNick();
        this.username = user.getUsername();
        this.signOn = user.getSignOn();
        this.isOp = user.isOp();
        this.isVoiced = user.isVoiced();
        this.channel = user.getChannel();
        this.hostname = user.getHostname();
        this.servername = user.getServername();
        this.isAway = user.isAway();
        this.realname = user.getRealname();
    }
