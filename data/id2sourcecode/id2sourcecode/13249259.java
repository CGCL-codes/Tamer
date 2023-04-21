    private static Handle selectApplication(boolean forSAT, byte[] selectAPDU, int slot) throws IOException {
        int channel;
        Slot cardSlot = slots[slot];
        if (!isAlive(cardSlot)) {
            throw new ConnectionNotFoundException("SmartCard not found");
        }
        if (!forSAT && (cardSlot.basicChannelInUse || cardSlot.SIMPresent)) {
            byte[] response = exchangeAPDU(cardSlot, cardSlot.getChannelAPDU);
            if (response.length == 2) {
                throw new IOException("No logical channel available");
            }
            channel = response[0];
        } else {
            cardSlot.basicChannelInUse = true;
            channel = 0;
        }
        selectAPDU[0] = (byte) ((selectAPDU[0] & 0xFC) | channel);
        byte[] result = exchangeAPDU(cardSlot, selectAPDU);
        int sw1 = result[result.length - 2] & 0xFF;
        int sw2 = result[result.length - 1] & 0xFF;
        if ((sw1 << 8) + sw2 != 0x9000) {
            closeChannel(cardSlot, channel);
            throw new ConnectionNotFoundException("Card application selection failed");
        }
        cardSlot.FCI = result;
        return new Handle(slot, channel);
    }
