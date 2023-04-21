    public void testSHA1() throws IOException {
        MessageDigestOutputStream out;
        out = MessageDigestOutputStream.makeSHA1();
        assertEquals("da 39 a3 ee 5e 6b 4b 0d 32 55 bf ef 95 60 18 90 af d8 07 09", TextUtil.toHexString(out.digest()));
        out = MessageDigestOutputStream.makeSHA1();
        out.writeAll(new FileInputStream("testFiles/hashfile.txt"));
        assertEquals("03 f6 9f fb c3 4b ff fc 16 31 c6 5f 28 ad 2a 91 09 19 60 3f", TextUtil.toHexString(out.digest()));
    }
