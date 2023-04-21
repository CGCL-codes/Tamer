    @Override
    public String description(Locale locale) {
        try {
            IMessagingChannel imesg = (IMessagingChannel) kernel().getChannel(IMessagingChannel.class);
            return imesg.getText("description", "i18n." + this.getClass().getName(), locale);
        } catch (ModuleNotFoundException m) {
            return "Plain Swemas module";
        }
    }
