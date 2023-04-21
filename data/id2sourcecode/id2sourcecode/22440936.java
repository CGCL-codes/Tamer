    public PublicKeyObject getPublicKeyObject(PublisherPublicKeyDigest desiredKeyID, KeyLocator locator, long timeout, CCNHandle handle) throws IOException {
        PublicKeyObject theKey = retrieve(locator.name().name(), locator.name().publisher());
        if ((null != theKey) && (theKey.available())) {
            Log.info(FAC_KEYS, "retrieved key {0} from cache.", locator.name().name());
            return theKey;
        }
        final int ITERATION_LIMIT = 5;
        final int TIMEOUT_ITERATION_LIMIT = 2;
        PublicKey publicKey = null;
        Interest keyInterest = new Interest(locator.name().name(), locator.name().publisher());
        keyInterest.minSuffixComponents(1);
        keyInterest.maxSuffixComponents(3);
        ContentObject retrievedContent = null;
        int iterationCount = 0;
        int timeoutCount = 0;
        IOException lastException = null;
        while ((null == publicKey) && (iterationCount < ITERATION_LIMIT)) {
            while ((null == retrievedContent) && (timeoutCount < TIMEOUT_ITERATION_LIMIT)) {
                try {
                    Log.fine(FAC_KEYS, "Trying network retrieval of key: {0} ", keyInterest.name());
                    retrievedContent = handle.get(keyInterest, timeout);
                } catch (IOException e) {
                    Log.warning(FAC_KEYS, "IOException attempting to retrieve key: {0}: {1}", keyInterest.name(), e.getMessage());
                    Log.warningStackTrace(e);
                    lastException = e;
                }
                if (null != retrievedContent) {
                    if (Log.isLoggable(FAC_KEYS, Level.INFO)) {
                        Log.info(FAC_KEYS, "Retrieved key {0} using locator {1}.", desiredKeyID, locator);
                        Log.info(FAC_KEYS, "Retrieved key {0} using locator {1} - got {2}", desiredKeyID, locator, retrievedContent.name());
                    }
                    break;
                }
                timeoutCount++;
            }
            if (null == retrievedContent) {
                Log.info(FAC_KEYS, "No data returned when we attempted to retrieve key using interest {0}, timeout {1} exception : {2}", keyInterest, timeout, ((null == lastException) ? "none" : lastException.getMessage()));
                if (null != lastException) {
                    throw lastException;
                }
                break;
            }
            if ((retrievedContent.signedInfo().getType().equals(ContentType.KEY)) || (retrievedContent.signedInfo().getType().equals(ContentType.LINK))) {
                theKey = new PublicKeyObject(retrievedContent, handle);
                if ((null != theKey) && (theKey.available())) {
                    if ((null != desiredKeyID) && (!theKey.publicKeyDigest().equals(desiredKeyID))) {
                        Log.fine(FAC_KEYS, "Got key at expected name {0} from locator {1}, but it wasn't the right key, wanted {2}, got {3}", retrievedContent.name(), locator, desiredKeyID, theKey.publicKeyDigest());
                    } else {
                        Log.info(FAC_KEYS, "Retrieved public key using name: {0}", locator.name().name());
                        remember(theKey);
                        return theKey;
                    }
                } else {
                    Log.severe(FAC_KEYS, "Decoded key at name {0} without error, but result was null!", retrievedContent.name());
                    throw new IOException("Decoded key at name " + retrievedContent.name() + " without error, but result was null!");
                }
            } else {
                Log.info(FAC_KEYS, "Retrieved an object when looking for key {0} at {1}, but type is {2}", locator.name().name(), retrievedContent.name(), retrievedContent.signedInfo().getTypeName());
            }
            Exclude currentExclude = keyInterest.exclude();
            if (null == currentExclude) {
                currentExclude = new Exclude();
            }
            currentExclude.add(new byte[][] { retrievedContent.digest() });
            keyInterest.exclude(currentExclude);
            iterationCount++;
        }
        return null;
    }
