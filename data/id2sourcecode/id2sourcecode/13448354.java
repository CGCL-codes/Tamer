    protected static MessageDownloaderResult processDownloadedFile05(final File tmpFile, final FcpResultGet results, final String logInfo) {
        try {
            logger.info("TOFDN: A message was downloaded." + logInfo);
            final byte[] metadata = results.getRawMetadata();
            if (tmpFile.length() == 0) {
                if (metadata != null && metadata.length > 0) {
                    logger.severe("TOFDN: Received metadata without data, maybe faked message." + logInfo);
                } else if (metadata == null || metadata.length == 0) {
                    logger.severe("TOFDN: Received neither metadata nor data, maybe a bug or a faked message." + logInfo);
                } else {
                    logger.severe("TOFDN: Received something, but bad things happened in code, maybe a bug or a faked message." + logInfo);
                }
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_MSG);
            }
            if (metadata == null) {
                final byte[] unzippedXml = FileAccess.readZipFileBinary(tmpFile);
                if (unzippedXml == null) {
                    logger.log(Level.SEVERE, "TOFDN: Unzip of unsigned xml failed." + logInfo);
                    tmpFile.delete();
                    return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_MSG);
                }
                FileAccess.writeFile(unzippedXml, tmpFile);
                try {
                    final MessageXmlFile currentMsg = new MessageXmlFile(tmpFile);
                    if (currentMsg.getFromName().indexOf('@') > -1) {
                        logger.severe("TOFDN: unsigned message has an invalid fromName (contains an @: '" + currentMsg.getFromName() + "'), message dropped." + logInfo);
                        tmpFile.delete();
                        return new MessageDownloaderResult(MessageDownloaderResult.INVALID_MSG);
                    }
                    currentMsg.setSignatureStatusOLD();
                    return new MessageDownloaderResult(currentMsg);
                } catch (final Exception ex) {
                    logger.log(Level.SEVERE, "TOFDN: Unsigned message is invalid." + logInfo, ex);
                    tmpFile.delete();
                    return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_MSG);
                }
            }
            MetaData _metaData = null;
            try {
                final Document doc = XMLTools.parseXmlContent(metadata, false);
                if (doc != null) {
                    _metaData = MetaData.getInstance(doc.getDocumentElement());
                }
            } catch (final Throwable t) {
                logger.log(Level.SEVERE, "TOFDN: Invalid metadata of signed message" + logInfo, t);
                _metaData = null;
            }
            if (_metaData == null) {
                logger.log(Level.SEVERE, "TOFDN: Metadata couldn't be read. " + "Offending file saved as badmetadata.xml - send to a dev for analysis." + logInfo);
                final File badmetadata = new File("badmetadata.xml");
                FileAccess.writeFile(metadata, badmetadata);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_METADATA);
            }
            if (_metaData.getType() != MetaData.SIGN && _metaData.getType() != MetaData.ENCRYPT) {
                logger.severe("TOFDN: Unknown type of metadata." + logInfo);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_METADATA);
            }
            final SignMetaData metaData = (SignMetaData) _metaData;
            final Identity owner = metaData.getPerson();
            if (!Core.getIdentities().isNewIdentityValid(owner)) {
                logger.severe("TOFDN: identity failed verification, message dropped." + logInfo);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.INVALID_MSG);
            }
            final byte[] plaintext = FileAccess.readByteArray(tmpFile);
            boolean sigIsValid = Core.getCrypto().detachedVerify(plaintext, owner.getPublicKey(), metaData.getSig());
            if (_metaData.getType() == MetaData.ENCRYPT) {
                final EncryptMetaData encMetaData = (EncryptMetaData) metaData;
                if (!Core.getIdentities().isMySelf(encMetaData.getRecipient())) {
                    logger.fine("TOFDN: Encrypted message was not for me.");
                    tmpFile.delete();
                    return new MessageDownloaderResult(MessageDownloaderResult.MSG_NOT_FOR_ME);
                }
                final LocalIdentity receiverId = Core.getIdentities().getLocalIdentity(encMetaData.getRecipient());
                final byte[] cipherText = FileAccess.readByteArray(tmpFile);
                final byte[] zipData = Core.getCrypto().decrypt(cipherText, receiverId.getPrivateKey());
                if (zipData == null) {
                    logger.severe("TOFDN: Encrypted message from " + encMetaData.getPerson().getUniqueName() + " could not be decrypted!" + logInfo);
                    tmpFile.delete();
                    return new MessageDownloaderResult(MessageDownloaderResult.DECRYPT_FAILED);
                }
                tmpFile.delete();
                FileAccess.writeFile(zipData, tmpFile);
                logger.fine("TOFDN: Decrypted an encrypted message for me, sender was " + encMetaData.getPerson().getUniqueName() + "." + logInfo);
            }
            final byte[] unzippedXml = FileAccess.readZipFileBinary(tmpFile);
            if (unzippedXml == null) {
                logger.severe("TOFDN: Unzip of signed xml failed." + logInfo);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_MSG);
            }
            FileAccess.writeFile(unzippedXml, tmpFile);
            MessageXmlFile currentMsg = null;
            try {
                currentMsg = new MessageXmlFile(tmpFile);
            } catch (final Exception ex) {
                logger.log(Level.SEVERE, "TOFDN: Exception when creating message object" + logInfo, ex);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.BROKEN_MSG);
            }
            if (!sigIsValid) {
                logger.severe("TOFDN: message failed verification, message dropped." + logInfo);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.INVALID_MSG);
            }
            final String metaDataHash = Mixed.makeFilename(Core.getCrypto().digest(metaData.getPerson().getPublicKey()));
            final String messageHash = Mixed.makeFilename(currentMsg.getFromName().substring(currentMsg.getFromName().indexOf("@") + 1, currentMsg.getFromName().length()));
            if (!metaDataHash.equals(messageHash)) {
                logger.severe("TOFDN: Hash in metadata doesn't match hash in message!\n" + "metadata : " + metaDataHash + " , message: " + messageHash + ". Message failed verification and was dropped." + logInfo);
                tmpFile.delete();
                return new MessageDownloaderResult(MessageDownloaderResult.INVALID_MSG);
            }
            currentMsg.setSignatureStatusVERIFIED_V2();
            return new MessageDownloaderResult(currentMsg, owner);
        } catch (final Throwable t) {
            logger.log(Level.SEVERE, "TOFDN: Exception thrown in downloadDate part 2." + logInfo, t);
        }
        tmpFile.delete();
        return null;
    }
