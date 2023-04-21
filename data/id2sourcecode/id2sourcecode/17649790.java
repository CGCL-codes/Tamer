    public void addModel(String model_file_name, String model_name) {
        logger.info("Add Mesh " + model_name + " to " + name);
        Node loaded_model = new Node(model_name);
        FormatConverter formatConverter = null;
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        String modelFormat = model_file_name.substring(model_file_name.lastIndexOf(".") + 1, model_file_name.length());
        String modelBinary = model_file_name.substring(0, model_file_name.lastIndexOf(".") + 1) + "jbin";
        URL model_url = ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_MODEL, model_file_name);
        URL model_url_jbin = ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_MODEL, modelBinary);
        if (model_url_jbin == null) {
            logger.info("Create jbin Format" + " (Node_" + name + " - Child_" + model_name + ")");
            if (modelFormat.equals("3ds") || modelFormat.equals("3DS")) {
                formatConverter = new MaxToJme();
            } else if (modelFormat.equals("md2")) {
                formatConverter = new Md2ToJme();
            } else if (modelFormat.equals("md3")) {
                formatConverter = new Md3ToJme();
            } else if (modelFormat.equals("ms3d")) {
                formatConverter = new MilkToJme();
            } else if (modelFormat.equals("ase")) {
                formatConverter = new AseToJme();
            } else if (modelFormat.equals("obj")) {
                formatConverter = new ObjToJme();
            }
            formatConverter.setProperty("mtllib", model_url);
            try {
                formatConverter.convert(model_url.openStream(), BO);
                loaded_model = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Found jbin Format" + " (Node_" + name + " - Child_" + model_name + ")");
            try {
                loaded_model = (Node) BinaryImporter.getInstance().load(model_url_jbin.openStream());
            } catch (IOException e) {
            }
        }
        Quaternion temp = new Quaternion();
        temp.fromAngleAxis(FastMath.PI / 2, new Vector3f(-1, 0, 0));
        loaded_model.setLocalRotation(temp);
        loaded_model.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
        removeMaterialStates(loaded_model);
        this.attachChild(loaded_model);
        if (loaded_model.getName().equals("TDS Scene")) {
            cleanNode("TDS Scene");
        }
        logger.info("Child_" + model_name + " has been added to Node_" + name);
    }
