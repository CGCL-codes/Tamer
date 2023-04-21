    public void write(int startAddr, byte[] writeBuf, int offset, int len) throws OneWireIOException, OneWireException {
        int i, room_left;
        if (len == 0) return;
        sp.checkSpeed();
        if ((startAddr + len) > size) throw new OneWireException("Write exceeds memory bank end");
        if (isReadOnly()) throw new OneWireException("Trying to write read-only memory bank");
        int startx = 0, nextx = 0;
        byte[] raw_buf = new byte[pageLength];
        byte[] extra_buf = new byte[sp.getExtraInfoLength()];
        int abs_addr = startPhysicalAddress + startAddr;
        int pl = pageLength;
        do {
            room_left = pl - ((abs_addr + startx) % pl);
            if ((len - startx) > room_left) nextx = startx + room_left; else nextx = len;
            sp.writeScratchpad(abs_addr + startx, writeBuf, offset + startx, nextx - startx);
            sp.readScratchpad(raw_buf, 0, pl, extra_buf);
            for (i = 0; i < (nextx - startx); i++) if (raw_buf[i] != writeBuf[i + offset + startx]) {
                sp.forceVerify();
                throw new OneWireIOException("Read back of scratchpad had incorrect data");
            }
            if ((((extra_buf[0] & 0x00FF) | ((extra_buf[1] << 8) & 0x00FF00)) & 0x00FFFF) != (abs_addr + startx)) {
                sp.forceVerify();
                throw new OneWireIOException("Address read back from scrachpad was incorrect");
            }
            sp.copyScratchpad(abs_addr + startx, nextx - startx);
            startx = nextx;
        } while (nextx < len);
    }
