    Slot(int slot) {
        this.slot = slot;
        this.cardSessionId = 1;
        this.locked = false;
        this.powered = false;
        this.SIMPresent = false;
        this.basicChannelInUse = false;
        this.respBuffer = new byte[respBufferSize];
        this.atr = null;
        this.FCI = null;
        this.getChannelAPDU = new byte[] { 0, 0x70, 0, 0, 1 };
        this.closeChannelAPDU = new byte[] { 0, 0x70, (byte) 0x80, 0 };
        this.getResponseAPDU = new byte[] { 0, (byte) 0xC0, 0, 0, 0 };
        this.isAliveAPDU = new byte[] { 0, 0x70, (byte) 0x80, 0 };
    }
