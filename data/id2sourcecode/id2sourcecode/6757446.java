    protected void assertStorableEquivalenceById(int id, Storage<StorableTestBasic> s1, Storage<StorableTestBasic> s2) throws FetchException {
        StorableTestBasic reader = s1.prepare();
        reader.setId(id);
        reader.load();
        StorableTestBasic writer = s2.prepare();
        writer.setId(id);
        writer.load();
        assertTrue(reader.equalProperties(writer));
    }
