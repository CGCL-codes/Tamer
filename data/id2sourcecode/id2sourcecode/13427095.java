    public void closeAllChannels() {
        if (!AppContext.instance().isUserLoggedIn()) {
            return;
        }
        Object lockObject = AppContext.instance().getLockObject();
        synchronized (lockObject) {
            User user = AppContext.instance().getUser();
            int channels = 0;
            for (Iterator i = getChatChannelIterator(); i.hasNext(); ) {
                ChatChannel channel = null;
                try {
                    channel = (ChatChannel) i.next();
                } catch (Exception x) {
                    x.printStackTrace();
                    continue;
                }
                UserLeaveCommand userLeaveCommand = new UserLeaveCommand(channel.getChannelId());
                IritgoEngine.instance().getAsyncCommandProcessor().perform(userLeaveCommand);
                ++channels;
            }
            if (channels != 0) {
                FlowControl flowControll = Engine.instance().getFlowControl();
                flowControll.add(new CountingFlowRule("shutdown.in.progress." + getTypeId(), channels));
            }
        }
    }
