    protected Identity(String name, final String key, final boolean createNew) {
        if (name.indexOf("@") < 0) {
            name = name + "@" + Core.getCrypto().digest(key);
        }
        name = Mixed.makeFilename(name);
        this.publicKey = key;
        this.uniqueName = name;
    }
