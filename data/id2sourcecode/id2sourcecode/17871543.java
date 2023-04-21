    public DigestInfo preSign(List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain, IdentityDTO identity, AddressDTO address, byte[] photo) throws NoSuchAlgorithmException {
        LOG.debug("preSign (ex)");
        String toBeSigned = identity.name + address.city;
        String digestAlgo = "SHA-1";
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute("IdentityName", identity.name);
        httpSession.setAttribute("IdentityCity", address.city);
        MessageDigest messageDigest = MessageDigest.getInstance(digestAlgo, new BouncyCastleProvider());
        byte[] digestValue = messageDigest.digest(toBeSigned.getBytes());
        String description = "Test Text Document";
        return new DigestInfo(digestValue, digestAlgo, description);
    }
