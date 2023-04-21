    public void receiveEvent(IRCEvent e) {
        if (e.getType() == Type.CONNECT_COMPLETE) {
            e.getSession().join("#jerklib");
        } else if (e.getType() == Type.CHANNEL_MESSAGE) {
            MessageEvent me = (MessageEvent) e;
            System.out.println(me.getNick() + ":" + me.getMessage());
            me.getChannel().say("Modes :" + me.getChannel().getUsersModes(me.getNick()).toString());
        } else if (e.getType() == Type.JOIN_COMPLETE) {
            JoinCompleteEvent jce = (JoinCompleteEvent) e;
            jce.getChannel().say("Hello from Jerklib!");
        } else {
            System.out.println(e.getType() + " " + e.getRawEventData());
        }
    }
