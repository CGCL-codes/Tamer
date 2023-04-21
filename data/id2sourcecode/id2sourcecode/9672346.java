    @Test
    public void digestInfoRipemd256() throws Exception {
        byte[] message = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("RIPEMD256", new BouncyCastleProvider());
        byte[] digest = messageDigest.digest(message);
        LOG.debug("Digest: " + new String(Hex.encodeHex(digest)));
        DERObjectIdentifier hashAlgoId = new DERObjectIdentifier("1.3.36.3.2.3");
        DigestInfo digestInfo = new DigestInfo(new AlgorithmIdentifier(hashAlgoId), digest);
        byte[] encodedDigestInfo = digestInfo.getEncoded();
        LOG.debug("Digest Info: " + new String(Hex.encodeHex(encodedDigestInfo)));
    }
