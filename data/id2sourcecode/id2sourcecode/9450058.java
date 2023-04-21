    public AppiaDataSession(AppiaProtocol proto, AppiaGroup group, Mailbox<Event> mbox, AppiaControlSession control, List<Channel> channels) {
        super(proto, group);
        mailbox = mbox;
        controlSession = control;
        worker = new PullPushWorker();
        worker.start();
        channelsMap = new HashMap<AppiaService, Channel>();
        servicesMap = new Hashtable<AppiaMessage, Object>();
        logger.debug("Number of channels: " + channels.size());
        for (Channel ch : channels) {
            logger.debug("Channel: " + ch.getChannelID());
            defaultSendService = new AppiaService(ch.getChannelID());
            channelsMap.put(defaultSendService, ch);
        }
        isSessionOpen = true;
    }
