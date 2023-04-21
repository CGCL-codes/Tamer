    public void run() throws ProtocolException {
        Iterator channelIter = mailstore.getChannels().iterator();
        while (channelIter.hasNext()) {
            Channel channel = (Channel) channelIter.next();
            if (channel.getExpectingReceipt()) {
                log.info("Searching for receipt " + channel.getReceiptAddress());
                FCPDocument doc = new FCPDocument(channel.getReceiptAddress());
                FCPRequester request = fcpManager.request(doc);
                if (request.getSuccess()) {
                    if (request.getDataFound()) {
                        log.fine("Receipt found.");
                        try {
                            EncryptedReceipt encryptedReceipt = new EncryptedReceipt(doc.getDataString(), channel.getNegotiationSecretKey(), channel.getNegotiationCryptoParams());
                            Receipt receipt = encryptedReceipt.getReceipt();
                            channel.setExpectingReceipt(false);
                            channel.setInChannelEnabled(true);
                            channel.configureInbound(receipt.getEe2erChannelName(), receipt.getEe2erSecretKey(), receipt.getEe2erCryptoParams(), 0);
                            channel.setOutChannelEnabled(true);
                            receipts.add(receipt);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                            log.fine(e.toString());
                        }
                    } else {
                        log.fine("Receipt not found.");
                    }
                }
            }
        }
    }
