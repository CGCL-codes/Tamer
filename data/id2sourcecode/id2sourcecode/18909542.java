    public PeerGroupID(byte[] seed) {
        super();
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException caught) {
            digester = null;
        }
        if (digester == null) {
            throw new ProviderException("SHA1 digest algorithm not found");
        }
        byte[] digest = digester.digest(seed);
        byte[] buf16 = new byte[16];
        System.arraycopy(digest, 0, buf16, 0, 16);
        UUID groupUUID = UUIDFactory.newUUID(buf16);
        id.longIntoBytes(PeerGroupID.groupIdOffset, groupUUID.getMostSignificantBits());
        id.longIntoBytes(PeerGroupID.groupIdOffset + 8, groupUUID.getLeastSignificantBits());
    }
