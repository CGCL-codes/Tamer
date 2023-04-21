    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        requestor = new DigitalPersonalIdentifier(requestorDpiStr);
        StubCtxDBManager dbMgr = new StubCtxDBManager();
        ICtxIdentifier ctxAttrId = dbMgr.createIdentifier(ctxAttrIdStr);
        dbMgr = null;
        readCtxPerm = new CtxPermission(ctxAttrId, "read");
        readWriteCtxPerm = new CtxPermission(ctxAttrId, "read,write");
        fooPerm = new FooPermission("foo", "bar");
    }
