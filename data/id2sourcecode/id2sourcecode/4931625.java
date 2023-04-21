    @Test
    public void testImplies() {
        CtxPermission entReadWritePerm = new CtxPermission(ctxEntityId, "read,write");
        CtxPermission entReadPerm = new CtxPermission(ctxEntityId, "read");
        assertTrue(entReadWritePerm.implies(entReadPerm));
        assertFalse(entReadPerm.implies(entReadWritePerm));
        CtxPermission attrReadPerm = new CtxPermission(ctxAttributeId, "read");
        assertTrue(entReadWritePerm.implies(attrReadPerm));
        CtxPermission attrWritePerm = new CtxPermission(ctxAttributeId, "write");
        assertFalse(entReadPerm.implies(attrWritePerm));
    }
