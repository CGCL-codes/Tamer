    @Override
    public UserPreferences getUserPreferences() {
        if (this.userPreferences == null) {
            URL codeBase = this.applet.getCodeBase();
            final String furnitureCatalogURLs = getAppletParameter(this.applet, FURNITURE_CATALOG_URLS_PARAMETER, "catalog.zip");
            final String texturesCatalogURLs = getAppletParameter(this.applet, TEXTURES_CATALOG_URLS_PARAMETER, "catalog.zip");
            final String readPreferencesURL = getAppletParameter(this.applet, READ_PREFERENCES_URL_PARAMETER, "");
            final String writePreferencesURL = getAppletParameter(this.applet, WRITE_PREFERENCES_URL_PARAMETER, "");
            final String userLanguage = getAppletParameter(this.applet, USER_LANGUAGE, null);
            this.userPreferences = new AppletUserPreferences(getURLs(codeBase, furnitureCatalogURLs), getURLs(codeBase, texturesCatalogURLs), getURLWithCodeBase(codeBase, writePreferencesURL), getURLWithCodeBase(codeBase, readPreferencesURL), userLanguage);
        }
        return this.userPreferences;
    }
