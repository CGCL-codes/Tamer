    public void write(int startAddr, byte[] writeBuf, int offset, int len) throws OneWireIOException, OneWireException {
        int i;
        byte[] es_data = new byte[3];
        byte[] scratchpad = new byte[8];
        if (len == 0) return;
        checkSpeed();
        if ((startAddr + len) > size) throw new OneWireException("Write exceeds memory bank end");
        if (isReadOnly() && (((startPhysicalAddress + startAddr) != 137) && (len != 1))) throw new OneWireException("Trying to write read-only memory bank");
        if (((startPhysicalAddress + startAddr) == 137) && (len == 1)) {
            ib.adapter.select(ib.address);
            byte[] buffer = new byte[5];
            buffer[0] = CHANNEL_ACCESS_WRITE;
            buffer[1] = writeBuf[offset];
            buffer[2] = (byte) ~writeBuf[offset];
            System.arraycopy(ffBlock, 0, buffer, 3, 2);
            ib.adapter.dataBlock(buffer, 0, 5);
            if (buffer[3] != (byte) 0x00AA) {
                throw new OneWireIOException("Failure to change DS2408 latch state.");
            }
        } else if (((startPhysicalAddress + startAddr) > 138) && ((startPhysicalAddress + startAddr + len) < 143)) {
            ib.adapter.select(ib.address);
            byte[] buffer = new byte[6];
            buffer[0] = (byte) 0xCC;
            buffer[1] = (byte) ((startAddr + startPhysicalAddress) & 0xFF);
            buffer[2] = (byte) ((((startAddr + startPhysicalAddress) & 0xFFFF) >>> 8) & 0xFF);
            System.arraycopy(writeBuf, offset, buffer, 3, len);
            ib.adapter.dataBlock(buffer, 0, len + 3);
        } else if (((startPhysicalAddress + startAddr) > 127) && ((startPhysicalAddress + startAddr + len) < 130)) {
            byte[] buffer = new byte[8];
            int addr = 128;
            byte[] buff = new byte[11];
            System.arraycopy(ffBlock, 0, buff, 0, 11);
            ib.adapter.select(ib.address);
            buff[0] = READ_MEMORY_COMMAND;
            buff[1] = (byte) (addr & 0xFF);
            buff[2] = (byte) (((addr & 0xFFFF) >>> 8) & 0xFF);
            ib.adapter.dataBlock(buff, 0, 11);
            System.arraycopy(buff, 3, buffer, 0, 8);
            System.arraycopy(writeBuf, offset, buffer, 0, len);
            if (!writeScratchpad(startPhysicalAddress + startAddr, buffer, 0, 8)) throw new OneWireIOException("Invalid CRC16 in write");
            if (!readScratchpad(scratchpad, 0, 8, es_data)) throw new OneWireIOException("Read scratchpad was not successful.");
            if ((es_data[2] & 0x20) == 0x20) {
                throw new OneWireIOException("The write scratchpad command was not completed.");
            } else {
                for (i = 0; i < 8; i++) if (scratchpad[i] != buffer[i]) {
                    throw new OneWireIOException("The read back of the data in the scratch pad did " + "not match.");
                }
            }
            copyScratchpad(es_data);
        } else throw new OneWireIOException("Trying to write read-only memory.");
    }
