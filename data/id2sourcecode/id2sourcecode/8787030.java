    @Test
    public void testDigest() throws Exception {
        assertNotNull("mac", mac);
        final byte[] message = "Hello".getBytes();
        final byte[] digest = mac.digest(message);
        assertNotNull("digest", digest);
        assertTrue("digest.length > 0", digest.length > 0);
        final byte[] digest2 = mac.digest(message);
        assertNotNull("digest2", digest);
        assertTrue("digest2.length > 0", digest.length > 0);
        assertArrayEquals("digests", digest, digest2);
    }
