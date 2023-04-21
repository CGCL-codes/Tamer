    int selectApplication(boolean forSAT, byte[] selectAPDU) throws IOException {
        int channel;
        if (!isAlive()) {
            if (cardSlot.isSAT()) {
                throw new ConnectionNotFoundException("SIM not found");
            } else {
                throw new ConnectionNotFoundException("SmartCard not found");
            }
        }
        if (cardSlot.initConnection()) {
            clean();
        }
        if (cardSlot.isSAT()) {
            SIMPresent = true;
        } else {
            SIMPresent = false;
        }
        if (!forSAT && (basicChannelInUse || SIMPresent)) {
            byte[] response = exchangeApdu(null, getChannelAPDU);
            if (response.length == 2) {
                throw new IOException("No logical channel available");
            }
            channel = response[0];
        } else {
            basicChannelInUse = true;
            channel = 0;
        }
        selectAPDU[0] = (byte) ((selectAPDU[0] & 0xFC) | channel);
        byte[] result = exchangeApdu(null, selectAPDU);
        int sw1 = result[result.length - 2] & 0xFF;
        int sw2 = result[result.length - 1] & 0xFF;
        if ((sw1 << 8) + sw2 != 0x9000) {
            closeChannel(channel);
            throw new ConnectionNotFoundException("Card application selection failed");
        }
        FCI = result;
        return channel;
    }
