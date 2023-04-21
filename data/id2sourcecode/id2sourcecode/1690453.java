    public void start() {
        Global.routeTable.addRoute("ServerCommands", Global.params().getParamString(ParamServer.PARAM_CHANNEL_UPDATE), UPDATE_NAMESPACE, ConnectionReader.TYPE_CLIENT, this);
        if (Global.serviceStatus != null) {
            ChannelUseDescriptionCollection cudc = Global.serviceStatus.getServiceDescription().getChannelUses();
            cudc.add(new ChannelUseDescription(Global.params().getParamString(ParamServer.PARAM_CHANNEL_UPDATE), "dynamic update of server parameters", 0L, 0L, null, new EventDescriptionCollection(new EventDescription(UPDATE_NAMESPACE, "specification of parameters to change"))));
            cudc.add(new ChannelUseDescription(Global.params().getParamString(ParamServer.PARAM_CHANNEL_REPORT), "reporting of the current setting of server parameters", 0L, 0L, new EventDescriptionCollection(new EventDescription(PARAMETER_NAMESPACE, "the current parameter setting")), null));
        }
        return;
    }
