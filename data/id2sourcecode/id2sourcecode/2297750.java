    public TriMesh loadModel(String modelName) {
        TriMesh model = null;
        try {
            ByteArrayOutputStream BO = new ByteArrayOutputStream();
            File modelFile = new File(localModelDirectory, modelName);
            if (!modelFile.exists() && !modelFile.canRead()) {
                modelFile = new File(globalModelDirectory, modelName);
            }
            InputStream modelStream = new FileInputStream(modelFile);
            FormatConverter converter = null;
            if (modelName.toLowerCase().indexOf(".3ds") > 0) {
                converter = new MaxToJme();
                converter.convert(new BufferedInputStream(modelStream), BO);
                Node node = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
                model = (TriMesh) node.getChild(0);
                Quaternion q = new Quaternion();
                q.fromAngles(-FastMath.HALF_PI, 0, 0);
                model.setLocalRotation(q);
            } else if (modelName.toLowerCase().indexOf(".obj") > 0) {
                converter = new ObjToJme();
                converter.convert(new BufferedInputStream(modelStream), BO);
                model = (TriMesh) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            } else {
                throw new Exception("Unknown Model Format");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model = new Box("PlaceHolder", new Vector3f(0f, 3f, 0f), 1f, 3f, 0.75f);
        }
        modelCache.put(modelName, model);
        return model;
    }
