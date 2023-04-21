    private void init() {
        if (Global.serviceStatus != null) {
            ChannelUseDescriptionCollection cudc = Global.serviceStatus.getServiceDescription().getChannelUses();
            cudc.add(new ChannelUseDescription(Global.params().getParamString(ParamServer.DISCOVERY_CHANNEL), "connection information so clients can discover the servers and their capabilities", 12L, 120L, new EventDescriptionCollection(new EventDescription(DISCOVERY_NAMESPACE, "information on services available from this server")), null));
        }
        return;
    }
