    public void write(int startAddr, byte[] writeBuf, int offset, int len) throws OneWireIOException, OneWireException {
        if (DEBUG) {
            Debug.debug("-----------------------------------------------------------");
            Debug.debug("MemoryBankSHAEE.write(int,byte[],int,int) called");
            Debug.debug("  startAddr=0x" + Convert.toHexString((byte) startAddr));
            Debug.debug("  writeBuf", writeBuf, offset, len);
            Debug.debug("  startPhysicalAddress=0x" + Convert.toHexString((byte) startPhysicalAddress));
        }
        int room_left;
        if (!checked) checked = ib.checkStatus();
        if (len == 0) return;
        checkSpeed();
        if (!ib.isContainerSecretSet()) throw new OneWireException("Secret is not set.");
        if ((startAddr + len) > size) throw new OneWireException("Write exceeds memory bank end");
        if (isReadOnly()) throw new OneWireException("Trying to write read-only memory bank");
        int startx = 0, nextx = 0;
        byte[] raw_buf = new byte[8];
        byte[] memory = new byte[size];
        int abs_addr = startPhysicalAddress + startAddr;
        int pl = 8;
        read(startAddr & 0xE0, false, memory, 0, size);
        if (abs_addr >= 128) {
            ib.getContainerSecret(memory, 0);
        }
        do {
            room_left = pl - ((abs_addr + startx) % pl);
            if ((len - startx) > room_left) nextx = startx + room_left; else nextx = len;
            if ((startx + startAddr) >= pageLength) System.arraycopy(memory, (((startx + startAddr) / 8) * 8) - 32, raw_buf, 0, 8); else System.arraycopy(memory, (((startx + startAddr) / 8) * 8), raw_buf, 0, 8);
            if ((nextx - startx) == 8) System.arraycopy(writeBuf, offset + startx, raw_buf, 0, 8); else if (((startAddr + nextx) % 8) == 0) System.arraycopy(writeBuf, offset + startx, raw_buf, ((startAddr + startx) % 8), 8 - ((startAddr + startx) % 8)); else System.arraycopy(writeBuf, offset + startx, raw_buf, ((startAddr + startx) % 8), ((startAddr + nextx) % 8) - ((startAddr + startx) % 8));
            scratchpad.writeScratchpad(abs_addr + startx + room_left - 8, raw_buf, 0, 8);
            scratchpad.copyScratchpad(abs_addr + startx + room_left - 8, raw_buf, 0, memory, 0);
            if ((startx + startAddr) >= pageLength) System.arraycopy(raw_buf, 0, memory, (((startx + startAddr) / 8) * 8) - 32, 8); else System.arraycopy(raw_buf, 0, memory, (((startx + startAddr) / 8) * 8), 8);
            startx = nextx;
        } while (nextx < len);
        if (DEBUG) Debug.debug("-----------------------------------------------------------");
    }
