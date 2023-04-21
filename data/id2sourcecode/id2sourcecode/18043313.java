    @Override
    public void changeLocale(Locale locale) {
        _locale = locale;
        try {
            IConfigProvidingChannel iconf = (IConfigProvidingChannel) kernel().getChannel(IConfigProvidingChannel.class);
            iconf.setItem("i18n.locale", locale.toString());
        } catch (ModuleNotFoundException e) {
        } catch (SwConfigurationFaultException e) {
        }
    }
