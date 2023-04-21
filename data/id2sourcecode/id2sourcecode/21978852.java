    public void testThumbWithNoInstances() {
        log.setLevel(org.apache.log4j.Level.DEBUG);
        org.apache.log4j.Logger photoLog = org.apache.log4j.Logger.getLogger(PhotoInfo.class.getName());
        photoLog.setLevel(org.apache.log4j.Level.DEBUG);
        PhotoInfo photo = PhotoInfo.create();
        Thumbnail thumb = photo.getThumbnail();
        assertTrue("getThumbnail should return error thumbnail", thumb == Thumbnail.getErrorThumbnail());
        assertEquals("No new instances should have been created", 0, photo.getNumInstances());
        File testFile = new File(testImgDir, "test1.jpg");
        if (!testFile.exists()) {
            fail("could not find test file " + testFile);
        }
        File instanceFile = VolumeBase.getDefaultVolume().getFilingFname(testFile);
        try {
            FileUtils.copyFile(testFile, instanceFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        photo.addInstance(VolumeBase.getDefaultVolume(), instanceFile, ImageInstance.INSTANCE_TYPE_ORIGINAL);
        Thumbnail thumb2 = photo.getThumbnail();
        log.setLevel(org.apache.log4j.Level.WARN);
        photoLog.setLevel(org.apache.log4j.Level.WARN);
        assertFalse("After instance addition, getThumbnail should not return default thumbnail", thumb == thumb2);
        assertEquals("There should be 2 instances: original & thumbnail", 2, photo.getNumInstances());
        photo.delete();
    }
