    public PublisherID(PublisherPublicKeyDigest keyID) {
        this(keyID.digest(), PublisherType.KEY);
    }
