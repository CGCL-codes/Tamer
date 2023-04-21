    private final boolean raytraceAgainstExistingImage(final FileInputStream sceneXML, final FileInputStream existingPPM, int reflectionDepth) {
        ISceneManager sceneManager = new SceneManager(new Camera());
        (new XMLSceneLoader()).load(sceneXML, "data/example_files/", sceneManager);
        ICamera camera = sceneManager.getCamera();
        if (camera == null) {
            camera = new Camera();
        }
        camera.setImageheight(1);
        camera.setImageWidth(1);
        camera.setZoomValue(1);
        camera.setPixelHeight(100);
        camera.setPixelWidth(100);
        sceneManager.setReflection(true);
        sceneManager.setReflectionDepth(reflectionDepth);
        Bitmap result = sceneManager.renderScene(2);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DigestOutputStream digestOutputStream = null;
        try {
            digestOutputStream = new DigestOutputStream(new FileOutputStream("data/tests/tmp.ppm"), messageDigest);
            (new PPMBitmapReaderWriter()).writeBitmap(digestOutputStream, result, PPMBitmapReaderWriter.PPM6_FILEFORMAT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        MessageDigest mdCompare = null;
        try {
            mdCompare = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DigestInputStream digestInputStream = new DigestInputStream(existingPPM, mdCompare);
        try {
            int i;
            while ((i = digestInputStream.read()) != -1) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MessageDigest.isEqual(messageDigest.digest(), mdCompare.digest())) {
            return true;
        }
        return false;
    }
