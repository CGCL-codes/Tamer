    static CtcpEvent ctcp(MessageEvent event, String ctcpString) {
        return new CtcpEventImpl(ctcpString, event.getHostName(), event.getMessage(), event.getNick(), event.getUserName(), event.getRawEventData(), event.getChannel(), event.getSession());
    }
