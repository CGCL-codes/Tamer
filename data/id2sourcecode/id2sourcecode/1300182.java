    public CodatID(PeerGroupID groupID, byte[] seed) {
        this();
        UUID groupCBID = groupID.getUUID();
        id.longIntoBytes(CodatID.groupIdOffset, groupCBID.getMostSignificantBits());
        id.longIntoBytes(CodatID.groupIdOffset + 8, groupCBID.getLeastSignificantBits());
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
        UUID peerCBID = UUIDFactory.newUUID(buf16);
        id.longIntoBytes(CodatID.idOffset, peerCBID.getMostSignificantBits());
        id.longIntoBytes(CodatID.idOffset + 8, peerCBID.getLeastSignificantBits());
    }
