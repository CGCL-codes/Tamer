    public static ID createResourceID(PeerGroupID pgID, String name) throws Exception {
        String seed = name + SEED;
        ID id = IDFactory.newContentID(pgID, true, Thumbprint.digest(seed.toLowerCase().getBytes(ENCODING)));
        return id;
    }
