    @Test
    public void testDigest() throws Exception {
        assertNotNull("mac", mac);
        final String message = Base64.encodeBase64String("Hello".getBytes());
        final String digest = mac.digest(message);
        assertNotNull("digest", digest);
        assertTrue("digest.length() > 0", digest.length() > 0);
        final String digest2 = mac.digest(message);
        assertNotNull("digest2", digest);
        assertTrue("digest2.length() > 0", digest.length() > 0);
        assertEquals("digests", digest, digest2);
    }
