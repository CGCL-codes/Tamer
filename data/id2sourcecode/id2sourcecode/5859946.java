    public PeerID(PeerGroupID groupID, byte[] seed) {
        this();
        UUID groupUUID = groupID.getUUID();
        id.longIntoBytes(PeerID.groupIdOffset, groupUUID.getMostSignificantBits());
        id.longIntoBytes(PeerID.groupIdOffset + 8, groupUUID.getLeastSignificantBits());
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
        UUID peerCBID = new UUID(buf16);
        id.longIntoBytes(PeerID.idOffset, peerCBID.getMostSignificantBits());
        id.longIntoBytes(PeerID.idOffset + 8, peerCBID.getLeastSignificantBits());
    }
