    @Test
    public void testDigestIsRight() {
        assertNotNull(digester);
        assertEquals("6f9b9af3cd6e8b8a73c2cdced37fe9f59226e27d", digester.digest("message"));
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", digester.digest(""));
    }
