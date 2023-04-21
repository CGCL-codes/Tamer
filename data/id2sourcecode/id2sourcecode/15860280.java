    @Test
    public void testDigest() {
        String digestValue = encrypter.digest("value to encrypt");
        assertEquals("ͷ����F�I�6�����", digestValue);
    }
