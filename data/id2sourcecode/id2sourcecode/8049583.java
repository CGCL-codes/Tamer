    public void run() throws ProtocolException {
        Iterator channelIter = mailstore.getChannels().iterator();
        while (channelIter.hasNext()) {
            Channel channel = (Channel) channelIter.next();
            if (channel.getOutChannelEnabled()) {
                log.info("Searching for messages from " + channel.getContactAlias() + ".");
                SlotRetriever slotRetriever = fcpManager.slotRetrieve("freenet:SSK@" + channel.getContactPublicSSKKey() + "/" + channel.getInChannelName() + "/", channel.getInLastMessageIndex() + 1, true);
                List documents = slotRetriever.getDocuments();
                channel.setInLastMessageIndex(slotRetriever.getLastFilledSlot());
                Iterator docIter = documents.iterator();
                while (docIter.hasNext()) {
                    FCPDocument doc = (FCPDocument) docIter.next();
                    try {
                        EncryptedMessage encryptedMessage = new EncryptedMessage(doc.getData(), channel.getInSecretKey(), channel.getInCryptoParams());
                        messages.add(encryptedMessage.getMessage());
                    } catch (DocumentException e) {
                        e.printStackTrace();
                        log.fine(e.toString());
                    }
                }
            }
        }
    }
