    public Server(Rirca main, JTabbedPane bt, Group group) {
        this.main = main;
        this.bt = bt;
        groupName = group.getGroupName();
        charset = group.getCharset();
        server = group.getServers();
        port = group.getPorts();
        serverPass = group.getServerPasses();
        channel = group.getChannels();
        nick = group.getNicks();
        nickPass = group.getNickPasses();
        username = group.getUsername();
        realname = group.getRealname();
    }
