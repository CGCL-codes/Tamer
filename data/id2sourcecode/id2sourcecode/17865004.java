    public ChatListener(BayeuxServer bayeuxServer, String name) {
        super(bayeuxServer, name);
        ServerChannel sc = bayeuxServer.getChannel("/" + name);
        if (sc != null) {
            sc.remove();
        }
        addService("/" + name, "processCometMessage");
        this.chatRoom = name;
    }
