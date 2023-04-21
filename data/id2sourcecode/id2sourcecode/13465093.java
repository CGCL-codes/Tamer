    @Test
    public void testMd52() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        long k1 = 906585445;
        long k2 = 179922739;
        ByteBuffer b = ByteBuffer.allocate(16);
        b.asIntBuffer().put((int) k1);
        System.out.println("b:" + DataUtil.byteToString(b.array()));
        b.position(4);
        b.asIntBuffer().put((int) k2);
        System.out.println("b:" + DataUtil.byteToString(b.array()));
        b.position(8);
        b.put("WjN}|M(6".getBytes());
        System.out.println("b:" + DataUtil.byteToString(b.array()));
        byte[] bbb = { 0x36, 0x09, 0x65, 0x65, 0x0A, (byte) 0xB9, 0x67, 0x33, 0x57, 0x6A, 0x4E, 0x7D, 0x7C, 0x4D, 0x28, 0x36 };
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] ddd = messageDigest.digest(bbb);
        System.out.println("bbb:" + new String(ddd, "iso8859_1"));
        System.out.println("bbb:" + DataUtil.byteToString(ddd));
    }
