    public void testMD5() throws IOException {
        MessageDigestOutputStream out;
        out = MessageDigestOutputStream.makeMD5();
        assertEquals("d4 1d 8c d9 8f 00 b2 04 e9 80 09 98 ec f8 42 7e", TextUtil.toHexString(out.digest()));
        out = MessageDigestOutputStream.makeMD5();
        out.writeAll(new FileInputStream("testFiles/hashfile.txt"));
        assertEquals("5d 35 ed 4a 2a 02 63 bf 48 c4 bc 27 36 09 a6 48", TextUtil.toHexString(out.digest()));
    }
