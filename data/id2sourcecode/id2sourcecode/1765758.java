    public static Spatial loadSpatial(String path, ModelFormat modelFormat) {
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        Object model = null;
        try {
            URL url = getResource(path);
            modelFormat.converter().setProperty("mtllib", url);
            modelFormat.converter().convert(url.openStream(), BO);
            model = new BinaryImporter().load(new ByteArrayInputStream(BO.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (Spatial) model;
    }
