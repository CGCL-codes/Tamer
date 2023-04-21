    public void write(int startAddr, byte[] writeBuf, int offset, int len) throws OneWireIOException, OneWireException {
        int i;
        byte result;
        if (len == 0) return;
        if (!ib.adapter.canProgram()) throw new OneWireException("Program voltage required but not available");
        if (isReadOnly()) throw new OneWireException("Trying to write read-only memory bank");
        if ((startAddr + len) > (pageLength * numberPages)) throw new OneWireException("Write exceeds memory bank end");
        ib.adapter.setProgramPulseDuration(DSPortAdapter.DELIVERY_EPROM);
        checkSpeed();
        boolean write_continue = false;
        for (i = 0; i < len; i++) {
            result = programByte(startAddr + i + startPhysicalAddress, writeBuf[offset + i], write_continue);
            if (writeVerification) {
                if ((byte) result == (byte) writeBuf[offset + i]) write_continue = true; else {
                    forceVerify();
                    throw new OneWireIOException("Read back byte on EPROM programming did not match");
                }
            }
        }
    }
