    @Test
    public void testStreamUpdate() throws Exception {
        ContentName testName = ContentName.fromNative(testHelper.getTestNamespace("testStreamUpdate"), collectionObjName);
        CCNHandle tHandle = CCNHandle.open();
        try {
            CollectionObject testCollectionObject = new CollectionObject(testName, small1, SaveType.RAW, tHandle);
            setupNamespace(testName);
            saveAndLog("testStreamUpdate", testCollectionObject, null, small1);
            System.out.println("testCollectionObject name: " + testCollectionObject.getVersionedName());
            CCNVersionedInputStream vis = new CCNVersionedInputStream(testCollectionObject.getVersionedName());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            while (!vis.eof()) {
                int read = vis.read(buf);
                if (read > 0) baos.write(buf, 0, read);
            }
            System.out.println("Read " + baos.toByteArray().length + " bytes, digest: " + DigestHelper.printBytes(DigestHelper.digest(baos.toByteArray()), 16));
            Collection decodedData = new Collection();
            decodedData.decode(baos.toByteArray());
            System.out.println("Decoded collection data: " + decodedData);
            Assert.assertEquals("Decoding via stream fails to give expected result!", decodedData, small1);
            CCNVersionedInputStream vis2 = new CCNVersionedInputStream(testCollectionObject.getVersionedName());
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            while (!vis2.eof()) {
                int val = vis2.read();
                if (val < 0) break;
                baos2.write((byte) val);
            }
            System.out.println("Read " + baos2.toByteArray().length + " bytes, digest: " + DigestHelper.printBytes(DigestHelper.digest(baos2.toByteArray()), 16));
            Assert.assertArrayEquals("Reading same object twice gets different results!", baos.toByteArray(), baos2.toByteArray());
            Collection decodedData2 = new Collection();
            decodedData2.decode(baos2.toByteArray());
            Assert.assertEquals("Decoding via stream byte read fails to give expected result!", decodedData2, small1);
            CCNVersionedInputStream vis3 = new CCNVersionedInputStream(testCollectionObject.getVersionedName());
            Collection decodedData3 = new Collection();
            decodedData3.decode(vis3);
            Assert.assertEquals("Decoding via stream full read fails to give expected result!", decodedData3, small1);
        } finally {
            removeNamespace(testName);
            tHandle.close();
        }
    }
