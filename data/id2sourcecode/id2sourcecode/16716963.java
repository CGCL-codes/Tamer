    @BeforeMethod
    public void setupPhoto() throws IOException, PhotovaultException, InstantiationException, IllegalAccessException {
        File testDir = new File(System.getProperty("basedir"), "testfiles");
        File testFile = new File(testDir, "test1.jpg");
        File dstFile = vol.getFilingFname(testFile);
        FileUtils.copyFile(testFile, dstFile);
        ImageFile imgFile = new ImageFile(dstFile);
        ImageFile oldFile = (ImageFile) session.get(ImageFile.class, imgFile.getId());
        OriginalImageDescriptor orig = null;
        if (oldFile != null) {
            imgFile = oldFile;
            orig = (OriginalImageDescriptor) imgFile.getImage("image#0");
        } else {
            session.save(imgFile);
            imgFile.addLocation(new FileLocation(vol, vol.mapFileToVolumeRelativeName(dstFile)));
            orig = new OriginalImageDescriptor(imgFile, "image#0");
            session.save(orig);
        }
        DTOResolverFactory rf = new HibernateDtoResolverFactory(session);
        VersionedObjectEditor<PhotoInfo> pe = new VersionedObjectEditor<PhotoInfo>(PhotoInfo.class, UUID.randomUUID(), rf);
        photo = pe.getTarget();
        session.save(photo);
        pe.setField("original", orig);
        PhotoEditor pep = (PhotoEditor) pe.getProxy();
        pep.setCropBounds(new Rectangle2D.Double(0.2, 0.2, 0.8, 0.8));
        pep.setPrefRotation(20.0);
        ChannelMapOperationFactory f = new ChannelMapOperationFactory(null);
        ColorCurve c = new ColorCurve();
        c.addPoint(0.0, 0.1);
        c.addPoint(1.0, 0.9);
        f.setChannelCurve("value", c);
        pep.setColorChannelMapping(f.create());
        pe.apply();
        session.flush();
    }
