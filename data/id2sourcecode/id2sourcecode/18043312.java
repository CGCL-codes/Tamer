    public SwLocaleProvider(IKernel kernel) {
        super(kernel);
        try {
            IConfigProvidingChannel iconf = (IConfigProvidingChannel) kernel.getChannel(IConfigProvidingChannel.class);
            String dlocale = iconf.getItem("i18n.locale");
            String[] spl = dlocale.split("_");
            String lang = spl[0], country = spl[1], variant = spl[2];
            if (lang != null && country != null && variant != null) _locale = new Locale(lang, country, variant); else if (lang != null && country != null) _locale = new Locale(lang, country); else if (lang != null) _locale = new Locale(lang); else _locale = new Locale("en", "US");
        } catch (ModuleNotFoundException e) {
            _locale = new Locale("en", "US");
        } catch (SwConfigurationFaultException e) {
            _locale = new Locale("en", "US");
        }
    }
