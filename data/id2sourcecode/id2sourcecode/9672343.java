    @Test
    public void digestInfoSha224() throws Exception {
        byte[] message = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-224", new BouncyCastleProvider());
        byte[] digest = messageDigest.digest(message);
        LOG.debug("Digest: " + new String(Hex.encodeHex(digest)));
        DERObjectIdentifier hashAlgoId = NISTObjectIdentifiers.id_sha224;
        DigestInfo digestInfo = new DigestInfo(new AlgorithmIdentifier(hashAlgoId), digest);
        byte[] encodedDigestInfo = digestInfo.getEncoded();
        LOG.debug("Digest Info: " + new String(Hex.encodeHex(encodedDigestInfo)));
    }
