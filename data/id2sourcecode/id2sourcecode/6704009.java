    public String description(Locale locale) {
        try {
            IMessagingChannel imesg = (IMessagingChannel) getChannel(IMessagingChannel.class);
            return imesg.getText("description", this.getClass().getName(), locale);
        } catch (ModuleNotFoundException e) {
            return "SwKernel version 0.01";
        }
    }
