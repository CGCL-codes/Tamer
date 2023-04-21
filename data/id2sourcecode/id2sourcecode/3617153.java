    public static boolean verify(ContentObject object, PublicKey publicKey) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, ContentEncodingException {
        if (null == publicKey) {
            throw new SignatureException("Cannot verify object without public key -- public key cannot be null!");
        }
        byte[] contentProxy = null;
        try {
            contentProxy = object.computeProxy();
        } catch (CertificateEncodingException e) {
            if (Log.isLoggable(Level.INFO)) Log.info("Encoding exception attempting to verify content digest for object: " + object.name() + ". Signature verification fails.");
            return false;
        }
        boolean result;
        if (null != contentProxy) {
            result = CCNSignatureHelper.verify(contentProxy, object.signature().signature(), object.signature().digestAlgorithm(), publicKey);
        } else {
            result = verify(object.name(), object.signedInfo(), object.content(), object.signature(), publicKey);
        }
        if ((!result) && Log.isLoggable(Log.FAC_VERIFY, Level.WARNING)) {
            Log.info("VERIFICATION FAILURE: " + object.name() + " timestamp: " + object.signedInfo().getTimestamp() + " content length: " + object.contentLength() + " ephemeral digest: " + DataUtils.printBytes(object.digest()) + " to be signed sha256 digest: " + DataUtils.printHexBytes(CCNDigestHelper.digest(object.prepareContent())));
            SystemConfiguration.outputDebugObject(object);
        }
        return result;
    }
