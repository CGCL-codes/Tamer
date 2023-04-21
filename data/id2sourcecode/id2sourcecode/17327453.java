    private void lookupConfiguration(ModuleContext moduleContext, Message message, ModuleData inputModuleData) throws NamingException, ModuleException, CPAException, HeaderMappingException {
        final Tracer tracer = baseTracer.entering("lookupConfiguration(ModuleContext moduleContext, Message message, ModuleData inputModuleData)");
        LookupManager lookupManager = LookupManager.getInstance();
        tracer.info("LookupManager lookupManager created");
        String channelID = moduleContext.getChannelID();
        checkValueForExistence(tracer, channelID, "channelID");
        Channel channel = (Channel) lookupManager.getCPAObject(CPAObjectType.CHANNEL, channelID);
        if (channel == null) ErrorHelper.logErrorAndThrow(tracer, "Channel object could not be retrieved. "); else tracer.info("Channel channel created : {0}", channel.getChannelName());
        Binding binding = lookupManager.getBindingByChannelId(channelID);
        if (binding == null) ErrorHelper.logErrorAndThrow(tracer, "Binding object could not be generated. "); else tracer.info("Binding binding created");
        String adapterType = channel.getAdapterType();
        tracer.info("Adapter type is: {0}", adapterType);
        if ((adapterType != null) && (adapterType.equals(EdifactUtil.ADAPTER_NAME))) {
            if (message.getMessageDirection().toString().equals(MessageDirection.OUTBOUND.toString())) realSettings = new ConfigurationEdifactAdapterToXiSapImpl(this.schemaStore, message, channel, binding, moduleContext); else realSettings = new ConfigurationEdifactAdapterFromXiSapImpl(this.schemaStore, message, channel, binding, moduleContext);
        } else {
            if (message.getMessageDirection().toString().equals(MessageDirection.OUTBOUND.toString())) realSettings = new ConfigurationGenericAdapterToXiSapImpl(this.schemaStore, message, channel, binding, moduleContext); else realSettings = new ConfigurationGenericAdapterFromXiSapImpl(this.schemaStore, message, channel, binding, moduleContext);
        }
        tracer.leaving();
    }
