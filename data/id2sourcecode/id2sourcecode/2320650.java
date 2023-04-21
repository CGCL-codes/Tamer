    private static void loadManifests() {
        try {
            Enumeration e = _classLoader.getResources("META-INF/MANIFEST.MF");
            while (e.hasMoreElements()) {
                URL url = (URL) e.nextElement();
                _manifests.add(new Manifest(url.openStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
