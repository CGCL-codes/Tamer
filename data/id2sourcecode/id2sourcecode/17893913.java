    public void testSHA256() throws IOException {
        MessageDigestOutputStream out;
        out = MessageDigestOutputStream.makeSHA256();
        assertEquals("e3 b0 c4 42 98 fc 1c 14 9a fb f4 c8 99 6f b9 24 27 ae 41 e4 64 9b 93 4c a4 95 99 1b 78 52 b8 55", TextUtil.toHexString(out.digest()));
        out = MessageDigestOutputStream.makeSHA256();
        out.writeAll(new FileInputStream("testFiles/hashfile.txt"));
        assertEquals("53 5e c1 74 99 d0 23 56 9a 11 78 4e cc 07 59 71 a2 03 ec 69 50 0c 9f cd 01 5a a2 88 eb 3a 2c 37", TextUtil.toHexString(out.digest()));
    }
