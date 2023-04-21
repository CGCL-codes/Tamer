    @Test
    public void testShaHash() throws Exception {
        setR();
        System.out.println("hash\t");
        Bignat r1_bn = Convenience.bn_from_bi(sizeInByte + bnOversize, r1);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1res = md.digest(r1_bn.get_digit_array());
        System.out.println(new String(BigIntUtil.to_byte_hex_string(new BigInteger(sha1res))));
        byte[] res1 = createAndReceive(applet, 0xb0, METHOD_SHA_HASH, GET_OUTPUT, 0x00, 0x00, sizeInByte, new byte[] { 0x01, 0x00, 0x01, 0x00 });
        System.out.println(new String(BigIntUtil.to_byte_hex_string(new BigInteger(sha1res))));
        byte[] resRev = new byte[sha1res.length];
        System.arraycopy(res1, 0, resRev, 0, resRev.length);
        assertArrayEquals(resRev, sha1res);
    }
