    private ContentObject getContentObject(ContentName name, PublisherPublicKeyDigest pub) throws ConfigurationException, InvalidKeyException, SignatureException, MalformedContentNameStringException {
        CCNTime now = CCNTime.now();
        ByteBuffer bb = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        bb.putLong(now.getTime());
        byte[] contents = bb.array();
        KeyLocator locator = new KeyLocator(ContentName.fromNative("/key/" + pub.digest().toString()));
        SignedInfo si = new SignedInfo(pub, now, SignedInfo.ContentType.DATA, locator);
        return new ContentObject(ContentName.fromNative(name, Long.toString(now.getTime())), si, contents, fakeSignature);
    }
