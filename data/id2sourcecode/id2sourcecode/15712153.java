    public static ContentObject isVersionedContentAvailable(ContentName contentName, ContentType desiredType, byte[] desiredContentDigest, PublisherPublicKeyDigest desiredPublisher, ContentVerifier verifier, long timeout, CCNHandle handle) throws IOException {
        if (timeout == 0) {
            return null;
        }
        byte[] contentNameVersionComponent = VersioningProfile.cutTerminalVersion(contentName).second();
        ContentObject retrievedObject = null;
        if (null == contentNameVersionComponent) {
            retrievedObject = VersioningProfile.getFirstBlockOfLatestVersion(contentName, null, desiredPublisher, timeout, ((null != verifier) ? verifier : handle.keyManager().getDefaultVerifier()), handle);
        } else {
            retrievedObject = SegmentationProfile.getSegment(contentName, null, desiredPublisher, timeout, ((null != verifier) ? verifier : handle.keyManager().getDefaultVerifier()), handle);
        }
        if (null == retrievedObject) {
            Log.info(Log.FAC_IO, "isContentAvailable: no content available corresponding to {0}", contentName);
            return null;
        } else {
            if (Log.isLoggable(Log.FAC_IO, Level.FINER)) Log.finer(Log.FAC_IO, "isContentAvailable: found content {0} matching name {1}", retrievedObject.name(), contentName);
            if (null != desiredContentDigest) {
                CCNInputStream inputStream = new CCNInputStream(retrievedObject, null, handle);
                byte[] streamContent = CCNDigestHelper.digest(inputStream);
                if (!Arrays.equals(streamContent, desiredContentDigest)) {
                    Log.info(Log.FAC_IO, "Retrieved content {0} matching name {1}, but that stream's content is {2}, not expected {3}.", retrievedObject.name(), contentName, DataUtils.printBytes(streamContent), DataUtils.printBytes(desiredContentDigest));
                    return null;
                }
            }
            return retrievedObject;
        }
    }
