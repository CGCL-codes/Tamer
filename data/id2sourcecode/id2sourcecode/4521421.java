    public void testSignedInfoString() throws Exception {
        String signedInfoStr = "<dsig:SignedInfo xmlns:dsig=\"http://www.w3.org/2000/09/xmldsig#\"><dsig:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" /><dsig:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><dsig:Reference URI=\"#uuid-7B20C5C0-9B85-35D1-590A-D1B3093451CF\"><dsig:Transforms><dsig:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /><dsig:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" /></dsig:Transforms><dsig:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><dsig:DigestValue>P834/zjB6jZbz80UPkCJQ+IGoqk=</dsig:DigestValue></dsig:Reference></dsig:SignedInfo>";
        Document signedInfoDoc = XmlUtils.parse(signedInfoStr);
        Element signedInfo = signedInfoDoc.getRootElement();
        byte[] bytes = XmlUtils.canonicalize(signedInfo, Canonicalizable.EXCLUSIVE_CANONICAL_XML);
        try {
            String b64EncodedDigest = CryptoUtils.digest(bytes, "SHA");
            String expected = "kP5B9dvJTnb+sSLDdMkgj+UYjJM=";
            assertEquals(expected, b64EncodedDigest);
        } catch (CryptoException e) {
            throw new CryptoException(e);
        }
    }
