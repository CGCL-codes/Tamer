    public void testImageInstanceUpdate() {
        File testFile = new File(testImgDir, "test1.jpg");
        File instanceFile = volume.getFilingFname(testFile);
        try {
            FileUtils.copyFile(testFile, instanceFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        ImageInstance f = ImageInstance.create(volume, instanceFile, photo, ImageInstance.INSTANCE_TYPE_ORIGINAL);
        assertNotNull("Image instance is null", f);
        int width = f.getWidth();
        int height = f.getHeight();
        int hist = f.getInstanceType();
        f.setHeight(height + 1);
        f.setWidth(width + 1);
        f.setInstanceType(ImageInstance.INSTANCE_TYPE_THUMBNAIL);
        File imgFile = f.getImageFile();
        assertMatchesDb(f);
        try {
            f = ImageInstance.retrieve(volume, imgFile.getName());
        } catch (PhotoNotFoundException e) {
            fail("Image file not found after update");
        }
        assertNotNull("Image instance is null", f);
        assertEquals("Width not updated", f.getWidth(), width + 1);
        assertEquals("height not updated", f.getHeight(), height + 1);
        assertEquals("Instance type not updated", f.getInstanceType(), ImageInstance.INSTANCE_TYPE_THUMBNAIL);
        File imgFile2 = f.getImageFile();
        assertTrue("Image file does not exist", imgFile2.exists());
        assertTrue("Image file name not same after update", imgFile.equals(imgFile2));
        f.delete();
    }
