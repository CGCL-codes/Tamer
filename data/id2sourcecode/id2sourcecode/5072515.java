    public void testImport() throws IOException, PhotoNotFoundException {
        File f = new File("testfiles/test_import.xml");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        XmlImporter importer = new XmlImporter(reader);
        TestImportListener l = new TestImportListener();
        importer.addListener(l);
        importer.importData();
        reader.close();
        assertFalse(l.error);
        assertEquals(XmlImporter.IMPORTING_COMPLETED, l.status);
        PhotoInfo p = PhotoInfo.retrievePhotoInfo(UUID.fromString("65bd68f7-79f4-463b-9e37-0a91182e6499"));
        assertEquals("NIKON D200", p.getCamera());
        assertEquals(8.0, p.getFStop());
        assertEquals("Digital", p.getFilm());
        assertEquals(100, p.getFilmSpeed());
        assertEquals(0, p.getQuality());
        ChannelMapOperation cm = p.getColorChannelMapping();
        ColorCurve c = cm.getChannelCurve("value");
        assertEquals(0.4, c.getY(1));
        assertEquals(0.5, c.getX(1));
        boolean foundOrig = false;
        for (int n = 0; n < p.getNumInstances(); n++) {
            ImageInstance i = p.getInstance(n);
            if (i.getInstanceType() == ImageInstance.INSTANCE_TYPE_ORIGINAL) {
                cm = i.getColorChannelMapping();
                c = cm.getChannelCurve("value");
                assertEquals(0.2, c.getY(1));
                assertEquals(0.6, c.getX(1));
                foundOrig = true;
            }
        }
        assertTrue(foundOrig);
        assertTrue(l.objects.contains(p));
        PhotoFolder folder = PhotoFolder.getFolderByUUID(UUID.fromString("06499cc6-d421-4262-8fa2-30a060982619"));
        assertEquals("test", folder.getName());
        PhotoFolder parent = folder.getParentFolder();
        assertEquals("extvol_deletetest", parent.getName());
        boolean found = false;
        for (int n = 0; n < folder.getPhotoCount(); n++) {
            if (folder.getPhoto(n) == p) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
