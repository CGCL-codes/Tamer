    public void testImageInstanceCreate() {
        File testFile = new File(testImgDir, "test1.jpg");
        File instanceFile = volume.getFilingFname(testFile);
        try {
            FileUtils.copyFile(testFile, instanceFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        ImageInstance f = ImageInstance.create(volume, instanceFile, photo, ImageInstance.INSTANCE_TYPE_ORIGINAL);
        assertNotNull("Image instance is null", f);
        assertMatchesDb(f);
        f.delete();
    }
