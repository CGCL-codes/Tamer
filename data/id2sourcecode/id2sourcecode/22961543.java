    protected static boolean prepareMessage05(final MessageUploaderWorkArea wa) {
        if (wa.senderId != null) {
            wa.message.setFromName(wa.senderId.getUniqueName());
            wa.message.signMessageV1(wa.senderId.getPrivateKey());
            wa.message.signMessageV2(wa.senderId.getPrivateKey());
            if (!wa.message.save()) {
                logger.severe("Save of signed msg failed. This was a HARD error, please report to a dev!");
                return false;
            }
        }
        FileAccess.writeZipFile(FileAccess.readByteArray(wa.unsentMessageFile), "entry", wa.uploadFile);
        if (!wa.uploadFile.isFile() || wa.uploadFile.length() == 0) {
            logger.severe("Error: zip of message xml file failed, result file not existing or empty. Please report to a dev!");
            return false;
        }
        if (wa.senderId != null) {
            final byte[] zipped = FileAccess.readByteArray(wa.uploadFile);
            if (wa.encryptForRecipient != null) {
                final byte[] encData = Core.getCrypto().encrypt(zipped, wa.encryptForRecipient.getPublicKey());
                if (encData == null) {
                    logger.severe("Error: could not encrypt the message, please report to a dev!");
                    return false;
                }
                wa.uploadFile.delete();
                FileAccess.writeFile(encData, wa.uploadFile);
                final EncryptMetaData ed = new EncryptMetaData(encData, wa.senderId, wa.encryptForRecipient.getUniqueName());
                wa.signMetadata = XMLTools.getRawXMLDocument(ed);
            } else {
                final SignMetaData md = new SignMetaData(zipped, wa.senderId);
                wa.signMetadata = XMLTools.getRawXMLDocument(md);
            }
        } else if (wa.encryptForRecipient != null) {
            logger.log(Level.SEVERE, "TOFUP: ALERT - can't encrypt message if sender is Anonymous! Will not send message!");
            return false;
        }
        long allLength = wa.uploadFile.length();
        if (wa.signMetadata != null) {
            allLength += wa.signMetadata.length;
        }
        if (allLength > 32767) {
            final Language language = Language.getInstance();
            final String title = language.getString("MessageUploader.messageToLargeError.title");
            final String txt = language.formatMessage("MessageUploader.messageToLargeError.text", Long.toString(allLength), Integer.toString(32767));
            JOptionPane.showMessageDialog(wa.parentFrame, txt, title, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
