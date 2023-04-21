    public void checkThem() {
        ChannelFactory.defaultFactory().init();
        goodPVs.clear();
        badPVs.clear();
        checkMap.clear();
        for (String name : thePVs) {
            Channel chan = ChannelFactory.defaultFactory.getChannel(name);
            if (chan.isConnected()) {
                checkMap.put(name, true);
            } else {
                chan.requestConnection();
                checkMap.put(name, false);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        for (Entry<String, Boolean> me : checkMap.entrySet()) {
            if (me.getValue()) goodPVs.add(me.getKey()); else badPVs.add(me.getKey());
        }
    }
