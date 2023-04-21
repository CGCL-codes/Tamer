    @Override
    public String name(Locale locale) {
        try {
            IMessagingChannel imesg = (IMessagingChannel) kernel().getChannel(IMessagingChannel.class);
            return imesg.getText("name", "i18n." + this.getClass().getName(), locale);
        } catch (ModuleNotFoundException m) {
            return this.getClass().getName();
        }
    }
