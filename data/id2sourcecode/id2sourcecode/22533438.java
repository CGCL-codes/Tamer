    public void addTrigger(String name, String type) {
        Channel ch = ChannelFactory.defaultFactory().getChannel(name);
        ch.connect();
        if (type.equals("slow")) {
            slowTrig.add(ch);
        } else if (type.equals("fast")) {
            fastTrig.add(ch);
        }
    }
