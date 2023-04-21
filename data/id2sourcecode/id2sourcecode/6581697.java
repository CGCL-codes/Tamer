    public final void testLongMessage() {
        int[] hash = { 0x34aa973c, 0xd4c4daa4, 0xf61eeb2b, 0xdbad2731, 0x6534016f };
        byte msgs[][] = new byte[][] { { 0x61 }, { 0x61, 0x61 }, { 0x61, 0x61, 0x61 }, { 0x61, 0x61, 0x61, 0x61 } };
        int lngs[] = new int[] { 1000000, 500000, 333333, 250000 };
        for (int n = 0; n < 4; n++) {
            for (int i = 0; i < lngs[n]; i++) {
                md.update(msgs[n]);
            }
            if (n == 2) {
                md.update(msgs[0]);
            }
            byte[] dgst = md.digest();
            for (int k = 0; k < 5; k++) {
                int i = k * 4;
                int j = ((dgst[i] & 0xff) << 24) | ((dgst[i + 1] & 0xff) << 16) | ((dgst[i + 2] & 0xff) << 8) | (dgst[i + 3] & 0xff);
                assertTrue("false: n =" + n + "  k=" + k + " j" + Integer.toHexString(j), hash[k] == j);
            }
        }
    }
