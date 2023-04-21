    @Test
    public void testImageCreationCmd() throws PhotovaultException, IOException {
        File testDir = new File(System.getProperty("basedir"), "testfiles");
        File testFile = new File(testDir, "test1.jpg");
        ModifyImageFileCommand cmd = new ModifyImageFileCommand(testFile);
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler(null);
        cmdHandler.executeCommand(cmd);
        ImageFile f = cmd.getImageFile();
        UUID fileId = f.getId();
        f = ifDAO.findById(fileId, false);
        assert f.getFileSize() == testFile.length();
        assert f.getImages().size() == 1;
        OriginalImageDescriptor img = (OriginalImageDescriptor) f.getImage("image#0");
        assertEquals(1, img.getPhotos().size());
        assertEquals(0, f.getLocations().size());
        VolumeDAO volDAO = daoFactory.getVolumeDAO();
        VolumeBase defaultVolume = vol1;
        File fl = defaultVolume.getFilingFname(testFile);
        FileUtils.copyFile(testFile, fl);
        cmd = new ModifyImageFileCommand(f);
        cmd.addLocation(new FileLocation(defaultVolume, defaultVolume.mapFileToVolumeRelativeName(fl)));
        cmdHandler.executeCommand(cmd);
        session.clear();
        f = cmd.getImageFile();
        fileId = f.getId();
        f = ifDAO.findById(fileId, false);
        assertEquals(1, f.getLocations().size());
        FileLocation loc = f.getLocations().iterator().next();
        assertEquals(loc.getVolume().getId(), defaultVolume.getId());
        assertEquals(loc.getFile().lastModified(), loc.getLastModified());
    }
