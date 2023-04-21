    public void sendTextMessageWithAttachmentsEncryptedAndSigned(String aText, String aFrom, String[] aRecipients, String aSubject, List<MailAttachment> aAttachments) throws EMailException {
        try {
            MimeMessage theMessage = prepareFor(aFrom, aRecipients, aSubject);
            MimeBodyPart theBodyPart = new MimeBodyPart();
            theBodyPart.setText(aText);
            File theDestinationFile = null;
            try {
                theDestinationFile = pgpCrypt(theBodyPart, aRecipients, true, true, aFrom);
                PGPMimeMultiPart theMultiPartMessage = PGPMimeMultiPart.createInstance();
                StringBuilder theBuilder = new StringBuilder();
                BufferedReader theReader = new BufferedReader(new FileReader(theDestinationFile));
                while (theReader.ready()) {
                    String theLine = theReader.readLine();
                    if (theLine != null) {
                        theBuilder.append(theLine + LINE_SEPARATOR);
                    }
                }
                theReader.close();
                theBodyPart = new MimeBodyPart();
                theBodyPart.setText(theBuilder.toString().trim());
                theMultiPartMessage.addBodyPart(theBodyPart);
                if (aAttachments != null) {
                    for (MailAttachment theAttachment : aAttachments) {
                        File theAttachmentFile = null;
                        try {
                            theAttachmentFile = pgpCrypt(theAttachment.getData(), aRecipients, false, true, aFrom);
                            theBodyPart = new MimeBodyPart();
                            theBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(new FileInputStream(theAttachmentFile), theAttachment.getContentType())));
                            theBodyPart.setFileName(theAttachment.getFileName() + ".gpg");
                            theMultiPartMessage.addBodyPart(theBodyPart);
                        } finally {
                            if (theAttachmentFile != null) {
                                theAttachmentFile.delete();
                            }
                        }
                    }
                }
                theMessage.setContent(theMultiPartMessage);
                Transport.send(theMessage);
            } finally {
                if (theDestinationFile != null) {
                    theDestinationFile.delete();
                }
            }
        } catch (EMailException e) {
            throw e;
        } catch (Exception e) {
            throw new EMailException("Fehler beim Senden der Mail", e);
        }
    }
