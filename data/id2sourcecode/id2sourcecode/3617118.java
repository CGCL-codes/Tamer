    public ContentObject(String digestAlgorithm, ContentName name, SignedInfo signedInfo, byte[] content, int offset, int length, Signature signature) {
        _name = name;
        _signedInfo = signedInfo;
        _content = new byte[length];
        if (null != content) System.arraycopy(content, offset, _content, 0, length);
        _signature = signature;
        if ((null != signature) && Log.isLoggable(Log.FAC_SIGNING, Level.FINEST)) {
            try {
                byte[] digest = CCNDigestHelper.digest(this.encode());
                byte[] tbsdigest = CCNDigestHelper.digest(prepareContent(name, signedInfo, content, offset, length));
                if (Log.isLoggable(Level.INFO)) {
                    Log.info("Created content object: " + name + " timestamp: " + signedInfo.getTimestamp() + " encoded digest: " + DataUtils.printBytes(digest) + " tbs content: " + DataUtils.printBytes(tbsdigest));
                    Log.info("Signature: " + this.signature());
                }
            } catch (Exception e) {
                if (Log.isLoggable(Level.WARNING)) {
                    Log.warning("Exception attempting to verify signature: " + e.getClass().getName() + ": " + e.getMessage());
                    Log.warningStackTrace(e);
                }
            }
        }
    }
