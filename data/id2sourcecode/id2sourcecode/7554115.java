    public void write(int startAddr, byte[] writeBuf, int offset, int len) throws OneWireIOException, OneWireException {
        int i, room_left;
        if (len == 0) return;
        if ((isPageLocked(0) && (startAddr < 32)) || (isPageLocked(1))) throw new OneWireIOException("The page is locked.");
        checkSpeed();
        if ((startAddr + len) > size) throw new OneWireException("Write exceeds memory bank end");
        if (isReadOnly()) throw new OneWireException("Trying to write read-only memory bank");
        int startx = 0, nextx = 0;
        byte[] raw_buf = new byte[8];
        byte[] memory = new byte[128];
        byte[] scratchpad = new byte[8];
        byte[] es_data = new byte[3];
        int abs_addr = startAddr;
        int pl = 8;
        read(0, false, memory, 0, 128);
        do {
            room_left = pl - ((abs_addr + startx) % pl);
            if ((len - startx) > room_left) nextx = startx + room_left; else nextx = len;
            System.arraycopy(memory, (((startx + startAddr) / 8) * 8), raw_buf, 0, 8);
            if ((nextx - startx) == 8) {
                System.arraycopy(writeBuf, offset + startx, raw_buf, 0, 8);
            } else {
                if (((startAddr + nextx) % 8) == 0) {
                    System.arraycopy(writeBuf, offset + startx, raw_buf, ((startAddr + startx) % 8), 8 - ((startAddr + startx) % 8));
                } else {
                    System.arraycopy(writeBuf, offset + startx, raw_buf, ((startAddr + startx) % 8), ((startAddr + nextx) % 8) - ((startAddr + startx) % 8));
                }
            }
            if (!writeScratchpad(abs_addr + startx + room_left - 8, raw_buf, 0, 8)) throw new OneWireIOException("Invalid CRC16 in write");
            if (!readScratchpad(scratchpad, 0, 8, es_data)) throw new OneWireIOException("Read scratchpad was not successful.");
            if ((es_data[2] & 0x20) == 0x20) {
                throw new OneWireIOException("The write scratchpad command was not completed.");
            } else {
                for (i = 0; i < 8; i++) if (scratchpad[i] != raw_buf[i]) {
                    throw new OneWireIOException("The read back of the data in the scratch pad did " + "not match.");
                }
            }
            copyScratchpad(es_data);
            if (startAddr >= pageLength) System.arraycopy(raw_buf, 0, memory, (((startx + startAddr) / 8) * 8) - 32, 8); else System.arraycopy(raw_buf, 0, memory, (((startx + startAddr) / 8) * 8), 8);
            startx = nextx;
        } while (nextx < len);
    }
