    public void testDigestResponse() throws IOException {
        byte[] digest1 = md5digest("0cc175b9c0f1b6a831c399e269772661");
        byte[] digest2 = md5digest("900150983cd24fb0d6963f7d28e17f72");
        OBEXAuthentication.DigestResponse expected = new OBEXAuthentication.DigestResponse();
        OBEXAuthentication.DigestResponse actual = new OBEXAuthentication.DigestResponse();
        expected.nonce = digest1;
        expected.requestDigest = digest2;
        actual.read(expected.write());
        assertEquals("DigestResponse 1", expected, actual);
        expected = new OBEXAuthentication.DigestResponse();
        actual = new OBEXAuthentication.DigestResponse();
        expected.nonce = digest2;
        expected.userName = new byte[] { 'b', 's' };
        expected.requestDigest = digest1;
        actual.read(expected.write());
        assertEquals("DigestResponse 2", expected, actual);
    }
