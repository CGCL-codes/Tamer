    public final void testClone() throws CloneNotSupportedException {
        MessageDigest clone = null;
        byte digest[];
        byte digestClone[];
        byte[] bytes = new byte[LENGTH];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        md.update(bytes, 0, LENGTH);
        clone = (MessageDigest) md.clone();
        digest = md.digest();
        digestClone = clone.digest();
        assertEquals("digest.length != digestClone.length :: " + digestClone.length, digest.length, digestClone.length);
        for (int i = 0; i < digest.length; i++) {
            assertEquals("digest[i] != digestClone[i] : i=" + i, digest[i], digestClone[i]);
        }
    }
