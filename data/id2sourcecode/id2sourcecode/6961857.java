    public void testSHAProvider() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] bytes = new byte[] { 1, 1, 1, 1, 1 };
        try {
            md.update(bytes, -1, 1);
            fail("No expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
        md.update(bytes, 1, -1);
        md = MessageDigest.getInstance("SHA");
        try {
            md.digest(bytes, 0, -1);
            fail("No expected DigestException");
        } catch (DigestException e) {
        }
        try {
            md.digest(bytes, -1, 0);
            fail("No expected DigestException");
        } catch (DigestException e) {
        }
    }
