    public void test_replicatingStorable() throws Exception {
        Repository altRepo = TestUtilities.buildTempRepository("alt");
        final Storage<StorableTestBasic> readage = getRepository().storageFor(StorableTestBasic.class);
        final Storage<StorableTestBasic> writage = altRepo.storageFor(StorableTestBasic.class);
        Storage<StorableTestBasic> wrappage = new ReplicatedStorage<StorableTestBasic>(getRepository(), readage, writage);
        StorableTestBasic replicator = wrappage.prepare();
        replicator.setId(1);
        setBasicProperties(replicator);
        replicator.insert();
        StorableTestBasic reader = load(readage, 1);
        StorableTestBasic writer = load(writage, 1);
        assertTrue(reader.equalProperties(writer));
        assertStorableEquivalenceById(1, readage, writage);
        replicator = wrappage.prepare();
        replicator.setId(1);
        replicator.setStringProp("updated");
        replicator.setLongProp(2342332);
        replicator.update();
        writer = load(writage, 1);
        reader = load(readage, 1);
        assertTrue(reader.equalProperties(writer));
        replicator.delete();
        try {
            reader.load();
            fail("successfully loaded deleted 'read' storable");
        } catch (FetchException e) {
        }
        try {
            writer.load();
            fail("successfully loaded deleted 'write' storable");
        } catch (FetchException e) {
        }
        StorableTestBasic replicator2 = wrappage.prepare();
        replicator2.setId(2);
        setBasicProperties(replicator2);
        replicator2.insert();
        replicator.setId(2);
        replicator.delete();
        try {
            load(readage, 2);
            fail("successfully loaded deleted 'read' storable 2");
        } catch (FetchException e) {
        }
        try {
            load(writage, 2);
            fail("successfully loaded deleted 'write' storable 2");
        } catch (FetchException e) {
        }
        altRepo.close();
        altRepo = null;
    }
