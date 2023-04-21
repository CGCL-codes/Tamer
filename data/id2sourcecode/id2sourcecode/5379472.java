    public void deploy(URL url) throws Throwable {
        InputStream is = url.openStream();
        List<MobletApp> apps = this.parseMobletApps(is);
        this.registry.register(apps);
    }
