    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        bob = new DigitalPersonalIdentifier("Bob");
        alice = new DigitalPersonalIdentifier("Alice");
        ICtxEntityIdentifier personId = new StubCtxEntityIdentifier("operatorId", "localServiceId", "person", 37L);
        ICtxAttributeIdentifier locationId = new StubCtxAttributeIdentifier(personId, "location", 666L);
        readLocation = new CtxPermission(locationId, "read");
        writeLocation = new CtxPermission(locationId, "write");
        readWriteLocation = new CtxPermission(locationId, "read,write");
    }
