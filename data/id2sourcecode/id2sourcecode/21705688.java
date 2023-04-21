    public LoadedScriptInfo loadScript(String name, URL url) throws ObolException, IOException {
        return this.loadScript(name, url.openStream());
    }
