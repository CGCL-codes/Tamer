    public String name(Locale locale) {
        try {
            IMessagingChannel imesg = (IMessagingChannel) getChannel(IMessagingChannel.class);
            return imesg.getText("name", this.getClass().getName(), locale);
        } catch (ModuleNotFoundException e) {
            return this.getClass().getName();
        }
    }
