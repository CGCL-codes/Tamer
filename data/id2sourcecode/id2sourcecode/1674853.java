    private void logVerboseChannelGroup(PersistChanGrpMgr bgroup, List l) {
        ChannelGroup g = bgroup.getChannelGroup();
        l.clear();
        String s = "Verbose dump of Group\n  group: " + g.toString() + "(" + g.getId() + ")\n";
        s = s + g.getChannels().toString() + "\n";
        Iterator chans = g.getChannels().iterator();
        while (chans.hasNext()) {
            Channel c = (Channel) chans.next();
            l.add(c);
            s = s + "Channel ids: " + c.getId() + " ";
        }
        s = s + "\n";
        logger.info(s);
    }
