    private TestMDLLoader() throws IOException {
        TextureProvider textureProvider = new TextureResourcePath("octlight/textures");
        URL url = ResourceUtil.getResource("octlight/models/iron_golem.mdl");
        Node node = TextMDLFile.parseModel(new InputStreamReader(url.openStream()), textureProvider);
        System.out.println("Model loaded...");
        getRootNode().addLight(new DirectionalLight(new Color(100, 100, 100), new Vector3(100, -100, -100)));
        getRootNode().addLight(new AmbientLight(new Color(1f, 1f, 1f)));
        getMouseGroup().addChild(new TransformGroup(node, Transform.createTranslateTransform(0, -20, 0).scaleThis(20).yRotateThis((float) Math.PI).xRotateThis((float) -Math.PI / 2)));
    }
