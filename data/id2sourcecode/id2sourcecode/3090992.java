    private void bootstrap() {
        String appDirName = System.getProperty("rpgmapper.app.dir", ".");
        appDir = new File(appDirName);
        appFile = null;
        if (appDir.exists()) {
            if (appDir.isDirectory()) {
                try {
                    appFile = new JarFile(new File(appDir, "rpgmapper.jar"));
                } catch (IOException ioe) {
                    throw new RuntimeException("Could not open application file.", ioe);
                }
            } else throw new RuntimeException("Application directory (" + appDirName + ") exists but is not a directory.");
        } else throw new RuntimeException("Application directory (" + appDirName + ") does not exist.");
        if (appFile == null) throw new RuntimeException("No application file available.");
        try {
            Locale locale = null;
            String localeProp = properties.getProperty("locale");
            if (localeProp != null) {
                String lang, country = "", var = "";
                int i = localeProp.indexOf("_");
                int o = 0;
                if (i >= 0) {
                    lang = localeProp.substring(0, i);
                    o = i + 1;
                } else lang = localeProp;
                i = localeProp.indexOf("_", o);
                if (i >= 0) {
                    country = localeProp.substring(o, i);
                    o = i + 1;
                    var = localeProp.substring(o);
                }
                logInfo("localization", "Got language code of " + lang + ":" + country + ":" + var);
                locale = new Locale(lang, country, var);
            }
            stringTable = new StringTable(appFile, locale);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load strings.", ioe);
        }
        appImage = Utilities.loadImage(appFile, "splash.png");
        SplashWindow splash = new SplashWindow(getString("app.name"), appImage, versionStr, Color.black, 250, 155, Color.black, 36, 177);
        splash.setVisible(true);
        splash.setActionString(getString("load.gui"));
        iconManager = new IconManager(appFile, "icons");
        menuManager = new MenuManager(this);
        windowManager = new WindowManager(this);
        menuManager.addMenuItem("<file>", new NewAction(this));
        menuManager.addMenuItem("<file>", new OpenAction(this));
        menuManager.addMenuItem("<file>", null);
        menuManager.addMenuItem("<file>", new SaveAction(this));
        menuManager.addMenuItem("<file>", new SaveAsAction(this));
        menuManager.addMenuItem("<file>", null);
        menuManager.addMenuItem("<file>", new PrintAction(this));
        menuManager.addMenuItem("<file>", new PrintPreviewAction(this));
        menuManager.addMenuItem("<file>", null);
        menuManager.addMenuItem("<file>", new ExitAction(this));
        menuManager.addMenuItem("<view>", new PaletteToggleAction(this));
        menuManager.addMenuItem("<view>", new ObjectPaletteToggleAction(this));
        menuManager.addMenuItem("<view>", new FloorAction(this));
        menuManager.addMenuItem("<tools>", new EditModeAction(this, "select", "select_menu.png", MapPanel.MODE_SELECT_OBJECTS));
        menuManager.addMenuItem("<tools>", new EditModeAction(this, "tileBrush", "tileBrush_menu.png", MapPanel.MODE_PAINT_TILES));
        menuManager.addMenuItem("<tools>", new EditModeAction(this, "addObject", "insertObject_menu.png", MapPanel.MODE_ADD_OBJECT));
        menuManager.addMenuItem("<tools>", null);
        menuManager.addMenuItem("<tools>", new DeleteAction(this));
        menuManager.addMenuItem("<plugin>", new PluginAction(this));
        menuManager.addMenuItem("<help>", new HelpAction(this));
        menuManager.addMenuItem("<help>", new AboutAction(this));
        mapObjectManager = new MapObjectManager();
        mapObjectManager.addMapObjectFactory(new CommentObject.CommentObjectFactory(this));
        splash.setActionString(getString("load.plugins"));
        loadPlugins();
        splash.setActionString(getString("load.plugins.system"));
        boolean sysPluginsOK = plugIn(systemPlugins);
        splash.setActionString(getString("load.plugins.user"));
        boolean usrPluginsOK = plugIn(userPlugins);
        splash.setActionString(getString("load.tiles"));
        tileSetManager = new TileSetManager();
        ZippedTileSet set = new ZippedTileSet(appFile, "tiles");
        tileSetManager.addTileSet(set);
        set = new ZippedTileSet(appFile, "hextiles");
        tileSetManager.addTileSet(set);
        if (appDir != null) {
            File tileDir = new File(appDir, "tilesets");
            if (tileDir.exists() && tileDir.isDirectory()) {
                File list[] = tileDir.listFiles();
                for (int i = 0; i < list.length; i++) try {
                    System.out.println(list[i].getName());
                    tileSetManager.addTileSet(new ZippedTileSet(new java.util.zip.ZipFile(list[i])));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else System.out.println("Tile set dir does not exist or is not a directory.");
        }
        tileSetManager.waitForLoad();
        if (!sysPluginsOK || !usrPluginsOK) {
            JOptionPane.showMessageDialog(null, getString("plugin.load.failed"), getString("plugin.load.failed.title"), JOptionPane.ERROR_MESSAGE);
        }
        new MapWindow(this, new Map(tileSetManager.getDefaultTileSet(), 10, 10));
        splash.setVisible(false);
        splash.dispose();
    }
