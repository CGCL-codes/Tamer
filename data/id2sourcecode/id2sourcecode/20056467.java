    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        IDigitalPersonalIdentifier aRequestor = new DigitalPersonalIdentifier(aRequestorDpiStr);
        IDigitalPersonalIdentifier anotherRequestor = new DigitalPersonalIdentifier(anotherRequestorDpiStr);
        StubCtxDBManager dbMgr = new StubCtxDBManager();
        ICtxIdentifier ctxAttrId = dbMgr.createIdentifier(ctxAttrIdStr);
        dbMgr = null;
        aDecision = new AccessControlDecision(aRequestor);
        aDecision.add(new CtxPermission(ctxAttrId, "read"));
        anotherDecision = new AccessControlDecision(anotherRequestor);
        anotherDecision.add(new CtxPermission(ctxAttrId, "read,write"));
        anotherDecision.add(new FooPermission("foo", "bar"));
    }
