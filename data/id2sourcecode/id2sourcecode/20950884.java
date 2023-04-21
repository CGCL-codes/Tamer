    public void initialize(PluginInterface pluginInterface) {
        this.pluginInterface = pluginInterface;
        log = pluginInterface.getLogger().getChannel("TrustValue Plugin");
        System.out.println("***************** starting trust trust plugin **********************");
        imgNoTrustValue = loadImage(resPath + "unrated.png");
        imgTrustValue = new Image[5];
        for (int i = 0; i < 5; i++) {
            imgTrustValue[i] = loadImage(resPath + "rated" + (i + 1) + ".png");
        }
        nick = pluginInterface.getPluginconfig().getPluginStringParameter("nick", "Anonymous");
        addPluginConfig();
        updater = new TrustValueUpdater(this);
        pluginInterface.addListener(this);
        addMyTorrentsColumn();
        addMyTorrentsMenu();
    }
