    @Test
    public void testEquals() {
        CtxPermission perm1 = new CtxPermission(ctxAttributeId, "read,write");
        CtxPermission perm2 = new CtxPermission(ctxAttributeId, "write,read");
        assertEquals(perm1.hashCode(), perm2.hashCode());
        assertTrue(perm1.equals(perm2));
        CtxPermission perm3 = new CtxPermission(ctxAttributeId, "read");
        assertFalse(perm1.equals(perm3));
        CtxPermission perm4 = new CtxPermission(ctxEntityId, "read");
        assertFalse(perm3.equals(perm4));
    }
