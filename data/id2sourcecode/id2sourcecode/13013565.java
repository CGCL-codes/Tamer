    public static Node loadJmeModel(String fileNameSrc) {
        try {
            URL urlModel = ModelUtils.class.getClassLoader().getResource(fileNameSrc);
            BufferedInputStream binaryReader = new BufferedInputStream(urlModel.openStream());
            Node model = (Node) BinaryImporter.getInstance().load(binaryReader);
            model.setModelBound(new BoundingBox());
            model.updateModelBound();
            return model;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
