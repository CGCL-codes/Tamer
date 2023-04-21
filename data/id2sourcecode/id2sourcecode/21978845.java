    public void testInstanceAddition() {
        File testFile = new File(testImgDir, "test1.jpg");
        File instanceFile = VolumeBase.getDefaultVolume().getFilingFname(testFile);
        try {
            FileUtils.copyFile(testFile, instanceFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        PhotoInfo photo = PhotoInfo.create();
        assertNotNull(photo);
        int numInstances = photo.getNumInstances();
        photo.addInstance(VolumeBase.getDefaultVolume(), instanceFile, ImageInstance.INSTANCE_TYPE_ORIGINAL);
        assertEquals(numInstances + 1, photo.getNumInstances());
        Vector instances = photo.getInstances();
        assertEquals(instances.size(), numInstances + 1);
        File testFile2 = new File(testImgDir, "test2.jpg");
        File instanceFile2 = VolumeBase.getDefaultVolume().getFilingFname(testFile2);
        try {
            FileUtils.copyFile(testFile2, instanceFile2);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        numInstances = photo.getNumInstances();
        ImageInstance inst = ImageInstance.create(VolumeBase.getDefaultVolume(), instanceFile2);
        photo.addInstance(inst);
        assertEquals(numInstances + 1, photo.getNumInstances());
        instances = photo.getInstances();
        assertEquals(instances.size(), numInstances + 1);
        boolean found1 = false;
        boolean found2 = false;
        for (int i = 0; i < photo.getNumInstances(); i++) {
            ImageInstance ifile = photo.getInstance(i);
            if (ifile.getImageFile().equals(instanceFile)) {
                found1 = true;
            }
            if (ifile.getImageFile().equals(instanceFile2)) {
                found2 = true;
            }
        }
        assertTrue("Image instance 1 not found", found1);
        assertTrue("Image instance 2 not found", found2);
        photo.delete();
    }
