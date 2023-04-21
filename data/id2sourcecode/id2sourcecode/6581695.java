    public final void testOneBlockMessage() {
        int[] words = new int[INDEX + 6];
        int[] hash1 = { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };
        int[] hash = { 0xA9993E36, 0x4706816A, 0xBA3E2571, 0x7850C26C, 0x9CD0D89D };
        for (int i = 0; i < words.length; i++) {
            words[i] = 0;
        }
        words[0] = 0x61626380;
        words[15] = 0x00000018;
        alternateHash(words, hash1);
        md.update(new byte[] { 0x61, 0x62, 0x63 });
        byte[] dgst = md.digest();
        for (int k = 0; k < 5; k++) {
            int i = k * 4;
            int j = ((dgst[i] & 0xff) << 24) | ((dgst[i + 1] & 0xff) << 16) | ((dgst[i + 2] & 0xff) << 8) | (dgst[i + 3] & 0xff);
            assertTrue("false1: k=" + k + " hash1[k]=" + Integer.toHexString(hash1[k]), hash[k] == hash1[k]);
            assertTrue("false2: k=" + k + " j=" + Integer.toHexString(j), hash[k] == j);
        }
    }
