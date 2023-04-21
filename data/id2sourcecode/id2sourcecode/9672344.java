    @Test
    public void digestInfoRipemd160() throws Exception {
        byte[] message = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("RIPEMD160", new BouncyCastleProvider());
        byte[] digest = messageDigest.digest(message);
        LOG.debug("Digest: " + new String(Hex.encodeHex(digest)));
        DERObjectIdentifier hashAlgoId = X509ObjectIdentifiers.ripemd160;
        DigestInfo digestInfo = new DigestInfo(new AlgorithmIdentifier(hashAlgoId), digest);
        byte[] encodedDigestInfo = digestInfo.getEncoded();
        LOG.debug("Digest Info: " + new String(Hex.encodeHex(encodedDigestInfo)));
    }
