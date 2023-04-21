    public Manifest getManifest() {
        if (manifest != null) return manifest;
        try {
            URL url = this.getResource("META-INF/MANIFEST.MF");
            if (url != null) return manifest = new Manifest(url.openStream()); else return null;
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
