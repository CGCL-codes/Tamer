    public AppletUserPreferences(URL[] pluginFurnitureCatalogURLs, URL[] pluginTexturesCatalogURLs, URL writePreferencesURL, URL readPreferencesURL, String userLanguage) {
        this.pluginFurnitureCatalogURLs = pluginFurnitureCatalogURLs;
        this.pluginTexturesCatalogURLs = pluginTexturesCatalogURLs;
        this.writePreferencesURL = writePreferencesURL;
        this.readPreferencesURL = readPreferencesURL;
        final Properties properties = getProperties();
        if (userLanguage == null) {
            userLanguage = getLanguage();
        }
        if (!Arrays.asList(getSupportedLanguages()).contains(userLanguage)) {
            userLanguage = Locale.ENGLISH.getLanguage();
        }
        setLanguage(properties.getProperty(LANGUAGE, userLanguage));
        setFurnitureCatalog(new DefaultFurnitureCatalog(pluginFurnitureCatalogURLs));
        setTexturesCatalog(new DefaultTexturesCatalog(pluginTexturesCatalogURLs));
        DefaultUserPreferences defaultPreferences = new DefaultUserPreferences();
        defaultPreferences.setLanguage(getLanguage());
        PatternsCatalog patternsCatalog = defaultPreferences.getPatternsCatalog();
        setPatternsCatalog(patternsCatalog);
        setUnit(LengthUnit.valueOf(properties.getProperty(UNIT, defaultPreferences.getLengthUnit().name())));
        setFurnitureCatalogViewedInTree(Boolean.parseBoolean(properties.getProperty(FURNITURE_CATALOG_VIEWED_IN_TREE, String.valueOf(defaultPreferences.isFurnitureCatalogViewedInTree()))));
        setNavigationPanelVisible(Boolean.parseBoolean(properties.getProperty(NAVIGATION_PANEL_VISIBLE, String.valueOf(defaultPreferences.isNavigationPanelVisible()))));
        setMagnetismEnabled(Boolean.parseBoolean(properties.getProperty(MAGNETISM_ENABLED, "true")));
        setRulersVisible(Boolean.parseBoolean(properties.getProperty(RULERS_VISIBLE, String.valueOf(defaultPreferences.isMagnetismEnabled()))));
        setGridVisible(Boolean.parseBoolean(properties.getProperty(GRID_VISIBLE, String.valueOf(defaultPreferences.isGridVisible()))));
        setFurnitureViewedFromTop(Boolean.parseBoolean(properties.getProperty(FURNITURE_VIEWED_FROM_TOP, String.valueOf(defaultPreferences.isFurnitureViewedFromTop()))));
        setFloorColoredOrTextured(Boolean.parseBoolean(properties.getProperty(ROOM_FLOOR_COLORED_OR_TEXTURED, String.valueOf(defaultPreferences.isRoomFloorColoredOrTextured()))));
        try {
            setWallPattern(patternsCatalog.getPattern(properties.getProperty(WALL_PATTERN, defaultPreferences.getWallPattern().getName())));
        } catch (IllegalArgumentException ex) {
            setWallPattern(defaultPreferences.getWallPattern());
        }
        setNewWallThickness(Float.parseFloat(properties.getProperty(NEW_WALL_THICKNESS, String.valueOf(defaultPreferences.getNewWallThickness()))));
        setNewWallHeight(Float.parseFloat(properties.getProperty(NEW_WALL_HEIGHT, String.valueOf(defaultPreferences.getNewWallHeight()))));
        setCurrency(defaultPreferences.getCurrency());
        List<String> recentHomes = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            String recentHome = properties.getProperty(RECENT_HOMES + i, null);
            if (recentHome != null) {
                recentHomes.add(recentHome);
            }
        }
        setRecentHomes(recentHomes);
        for (int i = 1; ; i++) {
            String ignoredActionTip = properties.getProperty(IGNORED_ACTION_TIP + i, "");
            if (ignoredActionTip.length() == 0) {
                break;
            } else {
                this.ignoredActionTips.put(ignoredActionTip, true);
            }
        }
        addPropertyChangeListener(Property.LANGUAGE, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateDefaultCatalogs();
            }
        });
    }
