    @Test
    public void testGetValidActions() {
        CtxPermission readPerm = new CtxPermission(ctxAttributeId, "read");
        assertEquals("read", readPerm.getActions());
        CtxPermission writePerm = new CtxPermission(ctxAttributeId, "write");
        assertEquals("write", writePerm.getActions());
        CtxPermission createPerm = new CtxPermission(ctxAttributeId, "create");
        assertEquals("create", createPerm.getActions());
        CtxPermission deletePerm = new CtxPermission(ctxAttributeId, "delete");
        assertEquals("delete", deletePerm.getActions());
        CtxPermission readWritePerm = new CtxPermission(ctxAttributeId, "write,read");
        assertEquals("read,write", readWritePerm.getActions());
        CtxPermission noPerm1 = new CtxPermission(ctxAssociationId, null);
        assertEquals("", noPerm1.getActions());
        CtxPermission noPerm2 = new CtxPermission(ctxEntityId, "");
        assertEquals("", noPerm2.getActions());
    }
