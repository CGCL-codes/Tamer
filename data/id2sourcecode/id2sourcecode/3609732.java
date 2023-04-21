    public static void logObject(Level level, String message, ContentObject co) {
        try {
            byte[] coDigest = CCNDigestHelper.digest(co.encode());
            byte[] tbsDigest = CCNDigestHelper.digest(ContentObject.prepareContent(co.name(), co.signedInfo(), co.content()));
            Log.log(level, message + " name: {0} timestamp: {1} digest: {2}  tbs: {3}.", co.name(), co.signedInfo().getTimestamp(), CCNDigestHelper.printBytes(coDigest, DEBUG_RADIX), CCNDigestHelper.printBytes(tbsDigest, DEBUG_RADIX));
        } catch (ContentEncodingException xs) {
            Log.log(level, "Cannot encode object for logging: {0}.", co.name());
        }
    }
