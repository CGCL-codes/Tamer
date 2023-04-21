    public final void testMultiBlockMessage() throws UnsupportedEncodingException {
        int[] hash = { 0x84983e44, 0x1c3bd26e, 0xbaae4aa1, 0xf95129e5, 0xe54670f1 };
        md.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes("UTF-8"));
        byte[] dgst = md.digest();
        for (int k = 0; k < 5; k++) {
            int i = k * 4;
            int j = ((dgst[i] & 0xff) << 24) | ((dgst[i + 1] & 0xff) << 16) | ((dgst[i + 2] & 0xff) << 8) | (dgst[i + 3] & 0xff);
            assertTrue("false: k=" + k + " j=" + Integer.toHexString(j), hash[k] == j);
        }
    }
