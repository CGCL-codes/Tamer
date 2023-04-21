    public LoadedScriptInfo loadScript(String name, String version, URL url) throws ObolException, IOException {
        return this.loadScript(name, version, url.openStream());
    }
