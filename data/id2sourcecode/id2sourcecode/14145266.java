    @Override
    protected byte[] computeBlockDigest(int leafIndex, byte[] content, int offset, int length) {
        byte[] blockDigest = null;
        try {
            blockDigest = CCNDigestHelper.digest(ContentObject.prepareContent(segmentName(leafIndex), segmentSignedInfo(leafIndex), content, offset, length));
            if (Log.isLoggable(Log.FAC_SIGNING, Level.INFO)) {
                Log.info("offset: " + offset + " block length: " + length + " blockDigest " + DataUtils.printBytes(blockDigest) + " content digest: " + DataUtils.printBytes(CCNDigestHelper.digest(content, offset, length)));
            }
        } catch (ContentEncodingException e) {
            Log.info("Exception in computeBlockDigest, leaf: " + leafIndex + " out of " + numLeaves() + " type: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return blockDigest;
    }
