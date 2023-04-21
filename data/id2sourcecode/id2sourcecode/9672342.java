    @Test
    public void digestInfoSha512() throws Exception {
        byte[] message = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] digest = messageDigest.digest(message);
        LOG.debug("Digest: " + new String(Hex.encodeHex(digest)));
        DERObjectIdentifier hashAlgoId = NISTObjectIdentifiers.id_sha512;
        DigestInfo digestInfo = new DigestInfo(new AlgorithmIdentifier(hashAlgoId), digest);
        byte[] encodedDigestInfo = digestInfo.getEncoded();
        LOG.debug("Digest Info: " + new String(Hex.encodeHex(encodedDigestInfo)));
    }
