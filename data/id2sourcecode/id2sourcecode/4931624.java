    @Test(expected = IllegalArgumentException.class)
    public void testGetMalformedActions() {
        new CtxPermission(ctxAttributeId, "read,write,eat");
    }
