    @Override
    public void decode(XMLDecoder decoder) throws ContentDecodingException {
        _publisherPublicKeyDigest = decoder.readBinaryElement(getElementLabel());
        if (null == _publisherPublicKeyDigest) {
            throw new ContentDecodingException("Cannot parse publisher key digest.");
        }
        if (_publisherPublicKeyDigest.length != PublisherID.PUBLISHER_ID_LEN) {
            if (Log.isLoggable(Level.WARNING)) {
                Log.warning("Wrong length for PublisherPublicKeyDigest: {0}", CCNDigestHelper.printBytes(_publisherPublicKeyDigest, 32));
            }
            _publisherPublicKeyDigest = new PublisherPublicKeyDigest(_publisherPublicKeyDigest).digest();
        }
    }
