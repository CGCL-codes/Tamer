    private ManifestLoader(ClassLoader classloader) throws RuntimeIoException {
        super();
        try {
            List<Manifest> mutableList = new ArrayList<Manifest>();
            Enumeration<URL> urls = classloader.getResources(JarFile.MANIFEST_NAME);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                InputStream inputStream = url.openStream();
                try {
                    Manifest manifest = new Manifest();
                    manifest.read(inputStream);
                    completeManifest(manifest, url);
                    mutableList.add(manifest);
                } finally {
                    inputStream.close();
                }
            }
            this.manifests = Collections.unmodifiableList(mutableList);
        } catch (IOException e) {
            throw new RuntimeIoException(e, IoMode.READ);
        }
    }
