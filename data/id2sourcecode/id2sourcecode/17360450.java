    @Test
    public void testAutomatedDereferenceForStreams() throws Exception {
        Link bigDataLink = new Link(bigData, "big", new LinkAuthenticator(new PublisherID(writeHandle.keyManager().getDefaultKeyID())));
        LinkObject bigDataLinkObject = new LinkObject(testHelper.getTestChildName("testAutomatedDereferenceForStreams", "bigDataLink"), bigDataLink, SaveType.REPOSITORY, writeHandle);
        bigDataLinkObject.save();
        Link twoHopLink = new Link(bigDataLinkObject.getBaseName());
        LinkObject twoHopLinkObject = new LinkObject(testHelper.getTestChildName("testAutomatedDereferenceForStreams", "twoHopLink"), twoHopLink, SaveType.REPOSITORY, writeHandle);
        twoHopLinkObject.save();
        CCNReader reader = new CCNReader(readHandle);
        byte[] bigDataReadback = reader.getVersionedData(bigDataLinkObject.getVersionedName(), null, SystemConfiguration.getDefaultTimeout());
        byte[] bdrdigest = CCNDigestHelper.digest(bigDataReadback);
        Log.info("Read back big data via link, got " + bigDataReadback.length + " bytes of an expected " + bigDataLength + ", digest match? " + (0 == DataUtils.compare(bdrdigest, bigValueDigest)));
        Assert.assertEquals(bigDataLength, bigDataReadback.length);
        Assert.assertArrayEquals(bdrdigest, bigValueDigest);
        byte[] bigDataReadback2 = reader.getVersionedData(twoHopLinkObject.getBaseName(), null, SystemConfiguration.getDefaultTimeout());
        byte[] bdr2digest = CCNDigestHelper.digest(bigDataReadback);
        Log.info("Read back big data via two links, got " + bigDataReadback2.length + " bytes of an expected " + bigDataLength + ", digest match? " + (0 == DataUtils.compare(bdr2digest, bigValueDigest)));
        Assert.assertEquals(bigDataLength, bigDataReadback2.length);
        Assert.assertArrayEquals(bdr2digest, bigValueDigest);
    }
