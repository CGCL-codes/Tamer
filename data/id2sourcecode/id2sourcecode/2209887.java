    private static ModelInstance loadBinaryCosmicSceneGraph(RRL rrl) {
        if (csgLoader == null) csgLoader = new CsgLoader();
        URL url = Repository.get().getResource(rrl);
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(url.openStream(), bufferSize));
        } catch (IOException ex) {
            Logger.getLogger(CosmicLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        String name = FileUtils.getFileNameWithoutExtension(rrl.getRelativePath());
        ModelInstance result = new ModelInstance(name);
        result.addChild(csgLoader.loadMain(dis));
        PMeshMaterial mat = Repository.get().getDefaultMaterial();
        PNodeUtils.setMaterialOnGraph(mat, result, true, true);
        result.setExternalReference(rrl);
        return result;
    }
