    public final void testReset() {
        byte[] bytes = new byte[LENGTH];
        byte[] digest1 = null;
        byte[] digest2 = null;
        boolean flag;
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (i & 0xFF);
        }
        digest1 = md.digest();
        md.update(bytes, 0, LENGTH);
        md.reset();
        digest2 = md.digest();
        flag = true;
        for (int i = 0; i < digest1.length; i++) {
            flag &= digest1[i] == digest2[i];
        }
        assertTrue("digests are not the same", flag);
    }
