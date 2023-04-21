    public static PipeID createPipeID(PeerGroupID pgID, String data) throws Exception {
        String seed = data + SEED;
        return IDFactory.newPipeID(pgID, Thumbprint.digest(seed.toLowerCase().getBytes(ENCODING)));
    }
