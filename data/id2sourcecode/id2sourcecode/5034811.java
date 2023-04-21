    public final void testDigestbyteArrayintint01() throws DigestException {
        int offset = 0;
        int len = 0;
        try {
            md.digest(null, 0, DIGESTLENGTH);
            fail("digest(null, 0, DIGESTLENGTH) : No IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        for (offset = -5; offset < 5; offset++) {
            try {
                md.digest(new byte[19], offset, 25);
                fail("md.digest(new byte[19],offset,25) :: offset=" + offset + " ::  No IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
        }
        for (len = -5; len < 5; len++) {
            try {
                md.digest(new byte[19], 25, len);
                fail("md.digest(new byte[19],25,len) :: len=" + len + " :: No IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
        }
        for (len = DIGESTLENGTH - 1; len >= 0; len--) {
            try {
                md.digest(new byte[DIGESTLENGTH], 0, len);
                fail("md.digest(new byte[DIGESTLENGTH], 0, len) :: len=" + len + " :: No DigestException");
            } catch (DigestException e) {
            }
        }
    }
