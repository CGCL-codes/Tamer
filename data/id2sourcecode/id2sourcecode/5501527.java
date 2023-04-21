    public static PublicKeyObject publishKeyToRepository(ContentName keyName, PublicKey keyToPublish, PublisherPublicKeyDigest signingKeyID, KeyLocator signingKeyLocator, long timeToWaitForPreexisting, boolean requirePublisherMatch, CCNHandle handle) throws IOException {
        PublisherPublicKeyDigest keyDigest = new PublisherPublicKeyDigest(keyToPublish);
        ContentObject availableContent = CCNReader.isVersionedContentAvailable(keyName, ContentType.KEY, keyDigest.digest(), (requirePublisherMatch ? signingKeyID : null), null, timeToWaitForPreexisting, handle);
        if ((SELF_SIGNED_KEY_LOCATOR == signingKeyLocator) && (null != availableContent)) {
            if (!PublicKeyObject.isSelfSigned(SegmentationProfile.segmentRoot(availableContent.name()), keyDigest, availableContent.signedInfo().getKeyLocator())) {
                if (Log.isLoggable(Log.FAC_KEYS, Level.INFO)) {
                    Log.info(Log.FAC_KEYS, "Found our key published under desired name {0}, but not self-signed as required - key locator is {1}.", availableContent.name(), availableContent.signedInfo().getKeyLocator());
                }
                availableContent = null;
            }
        }
        if (null != availableContent) {
            PublicKeyObject pko = new PublicKeyObject(availableContent, handle);
            RepositoryControl.localRepoSync(handle, pko);
            return pko;
        } else {
            PublicKeyObject publishedKey = publishKey(keyName, keyToPublish, signingKeyID, signingKeyLocator, null, SaveType.REPOSITORY, handle, handle.keyManager());
            if (Log.isLoggable(Log.FAC_KEYS, Level.INFO)) {
                Log.info(Log.FAC_KEYS, "Published key {0} from scratch as content {1}.", publishedKey.getVersionedName(), ContentName.componentPrintURI(publishedKey.getContentDigest()));
            }
            return publishedKey;
        }
    }
