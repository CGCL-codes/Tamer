    public void testWrapper() throws Exception {
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        Entry entry = new Entry("name", "value");
        EntryHolder holder = new EntryHolder(entry, "test", 10);
        StringWriter writer = new StringWriter();
        serializer.write(holder, writer);
        System.out.println(writer.toString());
        serializer.read(EntryHolder.class, writer.toString());
        System.err.println(writer.toString());
        String sourceXml = writer.toString();
        assertElementExists(sourceXml, "/entryHolder");
        assertElementHasAttribute(sourceXml, "/entryHolder", "code", "10");
        assertElementExists(sourceXml, "/entryHolder/entry");
        assertElementExists(sourceXml, "/entryHolder/entry/name");
        assertElementHasValue(sourceXml, "/entryHolder/entry/name", "name");
        assertElementExists(sourceXml, "/entryHolder/entry/value");
        assertElementHasValue(sourceXml, "/entryHolder/entry/value", "value");
        assertElementExists(sourceXml, "/entryHolder/name");
        assertElementHasValue(sourceXml, "/entryHolder/name", "test");
    }
