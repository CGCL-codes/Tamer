    private void init() {
        log = new DebugLogger("StatusReporter");
        Global.statusReporter = this;
        retiredStatus = new ArrayList<String>();
        totalMessages = new Counter();
        messagesByUser = new HashMap<String, Counter>();
        if (Global.serviceStatus != null) {
            ChannelUseDescriptionCollection cudc = Global.serviceStatus.getServiceDescription().getChannelUses();
            cudc.add(new ChannelUseDescription(Global.params().getParamString(ParamServer.STATUS_REPORTER_CHANNEL), "detailed status of server operation", 12L, 1200L, new EventDescriptionCollection(new EventDescription(HEARTBEAT_NAMESPACE, "detailed status of server operation")), null));
        }
    }
