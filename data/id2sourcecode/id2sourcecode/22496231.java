    public void testDigester() throws Exception {
        CredentialDigester digester = new CredentialDigester();
        digester.setAlgorithm("MD5");
        assertEquals("F5rUXGziy5fPECniEgRugQ==", digester.digest("testpass", null));
        digester.setBase16(true);
        assertEquals("179ad45c6ce2cb97cf1029e212046e81", digester.digest("testpass", null));
        digester.setBase16(false);
        digester.setAlgorithm("fubar");
        Exception e = null;
        try {
            digester.digest("testpass", null);
        } catch (NoSuchAlgorithmException e1) {
            e = e1;
        }
        assertNotNull(e);
    }
