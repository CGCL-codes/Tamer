    public static Collection<Manifest> getManifests(String path) {
        Collection<Manifest> result = new HashSet<Manifest>();
        try {
            List<URL> urls = Collections.list(PropertiesUtils.class.getClassLoader().getResources(path));
            for (URL url : urls) {
                InputStream is = url.openStream();
                try {
                    Manifest mf = new Manifest(is);
                    result.add(mf);
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
        return result;
    }
