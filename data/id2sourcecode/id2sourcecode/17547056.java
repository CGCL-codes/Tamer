    public PipeID(PeerGroupID groupID, byte[] seed) {
        this();
        UUID groupCBID = groupID.getUUID();
        id.longIntoBytes(PipeID.groupIdOffset, groupCBID.getMostSignificantBits());
        id.longIntoBytes(PipeID.groupIdOffset + 8, groupCBID.getLeastSignificantBits());
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
        UUID pipeCBID = UUIDFactory.newUUID(buf16);
        id.longIntoBytes(PipeID.idOffset, pipeCBID.getMostSignificantBits());
        id.longIntoBytes(PipeID.idOffset + 8, pipeCBID.getLeastSignificantBits());
    }
