    public static ParticleMesh loadJmeParticleMesh(String fileNameSrc) {
        try {
            URL urlModel = ModelUtils.class.getClassLoader().getResource(fileNameSrc);
            BufferedInputStream binaryReader = new BufferedInputStream(urlModel.openStream());
            ParticleMesh model = (ParticleMesh) BinaryImporter.getInstance().load(binaryReader);
            model.setModelBound(new BoundingBox());
            model.updateModelBound();
            return model;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
