    private void channelMode(IRCEvent event) {
        Pattern p = Pattern.compile("^\\S+\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(.+)$");
        Matcher m = p.matcher(event.getRawEventData());
        m.matches();
        event.getSession().getChannel(m.group(1)).setModeString(m.group(2));
        ModeEvent me = new ModeEventImpl(event.getRawEventData(), event.getSession(), null, null, event.getSession().getChannel(m.group(1)));
        manager.addToRelayList(me);
    }
