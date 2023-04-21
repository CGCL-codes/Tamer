    private boolean logIn() {
        System.out.println("begin: Player.logIn()");
        Jamud jamud = Jamud.currentInstance();
        jamud.playerManager().logPlayerMaskIn(this);
        for (Iterator i = channels.iterator(); i.hasNext(); ) {
            String n = (String) i.next();
            Channel c = (Channel) jamud.channelManager().getChannel(n);
            if (c != null) {
                try {
                    c.subscribe(this);
                } catch (InsufficientTrustException ite) {
                    channels.remove(n);
                }
            }
        }
        if (this.body == null) {
            this.body = this.nativebody;
        }
        if (this.body != null) {
            MudObjectContainer room = this.body.getParentContainer();
            if (room == null) {
                jamud.mudObjectRoot().addChildObject(this.body);
            } else {
                room.addChildObject(this.body);
            }
            this.body.addListener(this, PrintEvent.class);
        }
        this.state = STATE_INITIALIZED;
        print(jamud.getMessage(Jamud.MESSAGE_MOTD));
        if (this.body != null) {
            this.body.enact("look");
        } else {
            ready();
        }
        this.tickinit();
        System.out.println("end: Player.logIn()");
        return true;
    }
