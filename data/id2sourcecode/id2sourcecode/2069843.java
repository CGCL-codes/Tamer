    public void close() throws CardException {
        if (getChannelNumber() == 0) {
            throw new IllegalStateException("Cannot close basic logical channel");
        }
        if (isClosed) {
            return;
        }
        card.checkExclusive();
        try {
            byte[] com = new byte[] { 0x00, 0x70, (byte) 0x80, 0 };
            com[3] = (byte) getChannelNumber();
            setChannel(com);
            byte[] res = SCardTransmit(card.cardId, card.protocol, com, 0, com.length);
            if (isOK(res) == false) {
                throw new CardException("close() failed: " + PCSC.toString(res));
            }
        } catch (PCSCException e) {
            card.handleError(e);
            throw new CardException("Could not close channel", e);
        } finally {
            isClosed = true;
        }
    }
