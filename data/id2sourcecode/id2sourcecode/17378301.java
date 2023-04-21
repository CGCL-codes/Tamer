    protected static ContentObject getLocalLatestVersion(ContentName startingVersion, PublisherPublicKeyDigest publisher, long timeout, ContentVerifier verifier, CCNHandle handle, Long startingSegmentNumber, boolean findASegment) throws IOException {
        Log.info("getLocalLatestVersion: getting version later than {0} called with timeout: {1}", startingVersion, timeout);
        if (null == verifier) {
            verifier = handle.keyManager().getDefaultVerifier();
        }
        long attemptTimeout = SystemConfiguration.MEDIUM_TIMEOUT;
        if (timeout == SystemConfiguration.NO_TIMEOUT) {
        } else if (timeout > 0 && timeout < attemptTimeout) {
            attemptTimeout = timeout;
        }
        long stopTime = System.currentTimeMillis() + timeout;
        ContentName prefix = startingVersion;
        if (hasTerminalVersion(prefix)) {
            prefix = startingVersion.parent();
        }
        int versionedLength = prefix.count() + 1;
        ContentObject result = null;
        ArrayList<byte[]> excludeList = new ArrayList<byte[]>();
        while ((null == result) && ((timeout == SystemConfiguration.NO_TIMEOUT) || (System.currentTimeMillis() < stopTime))) {
            Interest getLatestInterest = null;
            if (findASegment) {
                getLatestInterest = firstBlockLatestVersionInterest(startingVersion, publisher);
            } else {
                getLatestInterest = latestVersionInterest(startingVersion, null, publisher);
            }
            if (excludeList.size() > 0) {
                byte[][] e = new byte[excludeList.size()][];
                excludeList.toArray(e);
                getLatestInterest.exclude().add(e);
            }
            result = handle.get(getLatestInterest, timeout);
            if (null != result) {
                if (Log.isLoggable(Level.INFO)) Log.info("gLLV getLocalLatestVersion: retrieved latest version object {0} type: {1}", result.name(), result.signedInfo().getTypeName());
                if (!verifier.verify(result)) {
                    Log.fine("gLLV result did not verify, trying to find a verifiable answer");
                    excludeList = addVersionToExcludes(excludeList, result.name());
                    Interest retry = new Interest(result.name(), publisher);
                    boolean verifyDone = false;
                    while (!verifyDone) {
                        if (retry.exclude() == null) retry.exclude(new Exclude());
                        retry.exclude().add(new byte[][] { result.digest() });
                        if (Log.isLoggable(Level.FINE)) {
                            Log.fine("gLLV result did not verify!  doing retry!! {0}", retry);
                            Log.fine("gLLVTime sending retry interest at {0}", System.currentTimeMillis());
                        }
                        result = handle.get(retry, attemptTimeout);
                        if (result != null) {
                            if (Log.isLoggable(Level.FINE)) Log.fine("gLLV we got something back: {0}", result.name());
                            if (verifier.verify(result)) {
                                Log.fine("gLLV the returned answer verifies");
                                verifyDone = true;
                            } else {
                                Log.fine("gLLV this answer did not verify either...  try again");
                            }
                        } else {
                            Log.fine("gLLV did not get a verifiable answer back");
                            verifyDone = true;
                        }
                    }
                }
                if (result != null) {
                    if (findASegment) {
                        if (VersioningProfile.isVersionedFirstSegment(prefix, result, startingSegmentNumber)) {
                            if (Log.isLoggable(Level.FINE)) Log.fine("getFirstBlockOfLatestVersion: got first block on first try: " + result.name());
                        } else {
                            ContentName notFirstBlockVersion = result.name().cut(versionedLength);
                            Log.info("CHILD SELECTOR FAILURE: getFirstBlockOfLatestVersion: Have version information, now querying first segment of " + startingVersion);
                            result = SegmentationProfile.getSegment(notFirstBlockVersion, startingSegmentNumber, null, timeout, verifier, handle);
                            if (result == null) {
                                Log.fine("gLV could not get the first segment of the version we just found...  should exclude the version");
                                excludeList = addVersionToExcludes(excludeList, notFirstBlockVersion);
                            }
                        }
                    } else {
                    }
                }
            }
        }
        if (result != null) {
            if (Log.isLoggable(Level.FINE)) Log.fine("gLLV returning: {0}", result.name());
        }
        return result;
    }
