    public static String idToString(PublisherPublicKeyDigest digest) {
        byte[] digested;
        digested = digest.digest();
        return ContentName.componentPrintURI(digested);
    }
