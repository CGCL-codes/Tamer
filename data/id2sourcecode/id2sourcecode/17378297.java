    private static ContentObject getLatestVersion(ContentName startingVersion, PublisherPublicKeyDigest publisher, long timeout, ContentVerifier verifier, CCNHandle handle, Long startingSegmentNumber, boolean findASegment) throws IOException {
        if (Log.isLoggable(Level.FINE)) {
            Log.fine("getFirstBlockOfLatestVersion: getting version later than {0} called with timeout: {1}", startingVersion, timeout);
        }
        if (null == verifier) {
            verifier = handle.keyManager().getDefaultVerifier();
        }
        long startTime = System.currentTimeMillis();
        long interestTime = 0;
        long elapsedTime = 0;
        long respondTime;
        long remainingTime = timeout;
        long attemptTimeout = SystemConfiguration.GLV_ATTEMPT_TIMEOUT;
        boolean noTimeout = false;
        if (timeout == SystemConfiguration.NO_TIMEOUT) {
            Log.finest("gLV called with NO_TIMEOUT");
            noTimeout = true;
        } else if (timeout == 0) {
            Log.finest("gLV called with timeout = 0, should just return the first thing we get");
        }
        ContentName prefix = startingVersion;
        if (hasTerminalVersion(prefix)) {
            prefix = startingVersion.parent();
        }
        int versionedLength = prefix.count() + 1;
        ContentObject result = null;
        ContentObject lastResult = null;
        ArrayList<byte[]> excludeList = new ArrayList<byte[]>();
        while ((remainingTime > 0 && elapsedTime < timeout) || (noTimeout || timeout == 0)) {
            Log.finer("gLV timeout: {0} remainingTime: {1} attemptTimeout: {2}", timeout, remainingTime, attemptTimeout);
            lastResult = result;
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
            Log.finer("timeout {0} startTime: {1} elapsedTime: {2} remainingTime: {3} new elapsedTime = {4}", timeout, startTime, elapsedTime, remainingTime, (System.currentTimeMillis() - startTime));
            interestTime = System.currentTimeMillis();
            long tempT;
            if (noTimeout) {
                tempT = timeout;
            } else if (timeout == 0) {
                tempT = attemptTimeout;
            } else {
                if (remainingTime < timeout - elapsedTime) {
                    tempT = remainingTime;
                } else {
                    tempT = timeout - elapsedTime;
                }
            }
            result = handle.get(getLatestInterest, tempT);
            elapsedTime = System.currentTimeMillis() - startTime;
            respondTime = System.currentTimeMillis() - interestTime;
            if (result == null && respondTime == 0) {
                Log.warning("gLV: handle.get returned null and did not wait the full timeout time for the object (timeout: {0} responseTime: {1}", timeout, respondTime);
                return null;
            }
            remainingTime = timeout - elapsedTime;
            if (Log.isLoggable(Level.FINE)) {
                Log.fine("gLV INTEREST: {0}", getLatestInterest);
                Log.fine("gLV trying handle.get with timeout: {0}", tempT);
                Log.fine("gLVTime sending Interest from gLV at {0} started at: {1}", System.currentTimeMillis(), startTime);
                Log.fine("gLVTime returned from handle.get in {0} ms", respondTime);
                Log.fine("gLV remaining time is now {0} ms", remainingTime);
            }
            if (null != result) {
                if (Log.isLoggable(Level.INFO)) Log.info("gLV getLatestVersion: retrieved latest version object {0} type: {1}", result.name(), result.signedInfo().getTypeName());
                if (!verifier.verify(result)) {
                    Log.fine("gLV result did not verify, trying to find a verifiable answer");
                    excludeList = addVersionToExcludes(excludeList, result.name());
                    Interest retry = new Interest(result.name(), publisher);
                    boolean verifyDone = false;
                    while (!verifyDone) {
                        if (retry.exclude() == null) retry.exclude(new Exclude());
                        retry.exclude().add(new byte[][] { result.digest() });
                        if (Log.isLoggable(Level.FINE)) {
                            Log.fine("gLV result did not verify!  doing retry!! {0}", retry);
                            Log.fine("gLVTime sending retry interest at {0}", System.currentTimeMillis());
                        }
                        result = handle.get(retry, attemptTimeout);
                        if (result != null) {
                            if (Log.isLoggable(Level.FINE)) Log.fine("gLV we got something back: {0}", result.name());
                            if (verifier.verify(result)) {
                                Log.fine("gLV the returned answer verifies");
                                verifyDone = true;
                            } else {
                                Log.fine("gLV this answer did not verify either...  try again");
                            }
                        } else {
                            Log.fine("gLV did not get a verifiable answer back");
                            verifyDone = true;
                        }
                    }
                    if (Log.isLoggable(Level.FINE)) {
                        Log.fine("the latest version did not verify and we might not have anything to send back...");
                        if (lastResult == null) Log.fine("lastResult is null...  we have nothing to send back"); else Log.fine("lastResult is NOT null, we have something to send back!");
                    }
                }
                if (result != null) {
                    if (findASegment) {
                        if (VersioningProfile.isVersionedFirstSegment(prefix, result, startingSegmentNumber)) {
                            if (Log.isLoggable(Level.FINE)) Log.fine("getFirstBlockOfLatestVersion: got first block on first try: " + result.name());
                        } else {
                            ContentName notFirstBlockVersion = result.name().cut(versionedLength);
                            Log.info("CHILD SELECTOR FAILURE: getFirstBlockOfLatestVersion: Have version information, now querying first segment of " + startingVersion);
                            result = SegmentationProfile.getSegment(notFirstBlockVersion, startingSegmentNumber, null, timeout - elapsedTime, verifier, handle);
                            if (result == null) {
                                Log.fine("gLV could not get the first segment of the version we just found...  should exclude the version");
                                excludeList = addVersionToExcludes(excludeList, notFirstBlockVersion);
                            }
                        }
                    } else {
                    }
                    if (result != null) {
                        lastResult = result;
                        if (noTimeout) {
                            timeout = attemptTimeout;
                            remainingTime = attemptTimeout;
                        } else if (timeout == 0) {
                            Log.fine("gLV we got an answer and the caller wants the first thing we found, returning");
                            return result;
                        } else if (remainingTime > 0) {
                            if (remainingTime > attemptTimeout) {
                                remainingTime = attemptTimeout;
                            } else {
                            }
                            Log.fine("gLV we still have time to try for a better answer: remaining time = {0}", remainingTime);
                        } else {
                            Log.fine("gLV time is up, return what we have");
                        }
                    } else {
                    }
                }
            }
            if (result == null) {
                if (respondTime == 0) {
                    Log.warning("gLV: handle.get returned null and did not wait the full timeout time for the object (timeout: {0} responseTime: {1}", timeout, respondTime);
                    return null;
                }
                Log.fine("gLV we didn't get anything");
                Log.info("getFirstBlockOfLatestVersion: no block available for later version of {0}", startingVersion);
                if (lastResult != null) {
                    if (Log.isLoggable(Level.FINE)) {
                        Log.fine("gLV returning the last result that wasn't null... ");
                        Log.fine("gLV returning: {0}", lastResult.name());
                    }
                    return lastResult;
                } else {
                    Log.fine("gLV we didn't get anything, and we haven't had anything at all... try with remaining long timeout");
                    if (remainingTime > 0) {
                        Log.fine("we did not get anything back from our interest, but we still have time remaining.  timeout: {0} elapsedTime {1} remainingTime {2}", timeout, elapsedTime, remainingTime);
                        timeout = remainingTime;
                    }
                }
            }
            if (result != null) startingVersion = SegmentationProfile.segmentRoot(result.name());
        }
        if (result != null) {
            if (Log.isLoggable(Level.FINE)) Log.fine("gLV returning: {0}", result.name());
        }
        return result;
    }
