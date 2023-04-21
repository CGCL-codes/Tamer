    Cad(int slot, SecurityToken securityToken) throws IOException {
        try {
            cardSlot = SlotFactory.getCardSlot(slot, securityToken);
            if (cardSlot == null) {
                throw new IOException("Slot factory could not create a slot");
            }
        } catch (CardDeviceException e) {
            throw new IOException("Config error: " + e.getMessage());
        }
        slotN = slot;
        getChannelAPDU = new byte[] { 0, 0x70, 0, 0, 1 };
        closeChannelAPDU = new byte[] { 0, 0x70, (byte) 0x80, 0 };
        getResponseAPDU = new byte[] { 0, (byte) 0xC0, 0, 0, 0 };
        case2APDU = new byte[5];
    }
