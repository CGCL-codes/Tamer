    private boolean initialize() {
        if (board.isWriteAccessBoard()) {
            privateKey = board.getPrivateKey();
            publicKey = board.getPublicKey();
            secure = true;
        } else {
            secure = false;
        }
        logger.info("TOFUP: Uploading message to board '" + board.toString() + "' with HTL " + messageUploadHtl);
        if (!saveMessage(message, messageFile)) {
            logger.severe("This was a HARD error and the file to upload is lost, please report to a dev!");
            return false;
        }
        if (!uploadAttachments(message, messageFile)) {
            return false;
        }
        zipFile = new File(messageFile.getPath() + ".upltmp");
        zipFile.delete();
        zipFile.deleteOnExit();
        FileAccess.writeZipFile(FileAccess.readByteArray(messageFile), "entry", zipFile);
        String sender = message.getFrom();
        String myId = identities.getMyId().getUniqueName();
        if (sender.equals(myId) || sender.equals(Mixed.makeFilename(myId))) {
            byte[] zipped = FileAccess.readByteArray(zipFile);
            SignMetaData md = new SignMetaData(zipped, identities.getMyId());
            signMetadata = XMLTools.getRawXMLDocument(md);
        }
        return true;
    }
