    public void addLocale(java.net.URL p_url) {
        logger.info("adding locale from file " + p_url.toString());
        Properties localeProp = new java.util.Properties();
        try {
            InputStream in = new BufferedInputStream(p_url.openStream());
            localeProp.loadFromXML(in);
        } catch (java.io.IOException exp) {
            logger.info(exp.getMessage());
            return;
        }
        String localeCode = localeProp.getProperty("LocaleLanguageCode");
        String localeCountry = localeProp.getProperty("LocaleCountry");
        String localeVariant = localeProp.getProperty("LocaleVariant");
        Locale newLocale = null;
        if ((localeCode != null) && (localeCountry != null) && (localeVariant != null)) {
            newLocale = new Locale(localeCode, localeCountry, localeVariant);
        } else {
            if ((localeCode != null) && (localeCountry != null)) {
                newLocale = new Locale(localeCode, localeCountry);
            } else {
                if ((localeCode != null)) {
                    newLocale = new Locale(localeCode);
                }
            }
        }
        locales.put(localeCode, newLocale);
        localesProps.put(localeCode, localeProp);
        logger.info("Locale added is  " + newLocale.toString());
    }
