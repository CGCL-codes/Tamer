    private static Manifest getManifest() throws IOException {
        Stack<URL> manifests = new Stack<URL>();
        for (Enumeration<URL> e = Bootstrap.class.getClassLoader().getResources(MANIFEST); e.hasMoreElements(); ) {
            manifests.add(e.nextElement());
        }
        while (!manifests.isEmpty()) {
            URL url = manifests.pop();
            InputStream in = url.openStream();
            Manifest manifest = new Manifest(in);
            in.close();
            if (manifest.getMainAttributes().getValue(BOOTSTRAP_CLASS) != null) {
                return manifest;
            }
        }
        throw new Error("No " + MANIFEST + " with " + BOOTSTRAP_CLASS + " found");
    }
