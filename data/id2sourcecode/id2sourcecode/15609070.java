    public boolean readAuthenticatedPage(int page, byte[] data, int dataStart, byte[] extra_info, int extraStart) throws OneWireException, OneWireIOException {
        byte[] send_block = new byte[40];
        byte[] challenge = new byte[8];
        int addr = (page * pageLength) + startPhysicalAddress;
        ib.getChallenge(challenge, 4);
        scratchpad.writeScratchpad(addr, challenge, 0, 8);
        if (!adapter.select(ib.getAddress())) throw new OneWireIOException("Device select failed.");
        send_block[0] = READ_AUTH_PAGE;
        send_block[1] = (byte) (addr & 0xFF);
        send_block[2] = (byte) (((addr & 0xFFFF) >>> 8) & 0xFF);
        System.arraycopy(ffBlock, 0, send_block, 3, 35);
        adapter.dataBlock(send_block, 0, 38);
        if (DEBUG) {
            IOHelper.writeLine("-------------------------------------------------------------");
            IOHelper.writeLine("ReadAuthPage - send_block:");
            IOHelper.writeBytesHex(send_block, 0, 38);
        }
        if (CRC16.compute(send_block, 0, 38, 0) != 0x0000B001) {
            throw new OneWireException("First CRC didn't pass.");
        }
        System.arraycopy(send_block, 3, data, dataStart, 32);
        System.arraycopy(ffBlock, 0, send_block, 0, 22);
        try {
            Thread.sleep(2);
        } catch (InterruptedException ie) {
            ;
        }
        adapter.dataBlock(send_block, 0, 22);
        if (DEBUG) {
            IOHelper.writeLine("ReadAuthPage - MAC:");
            IOHelper.writeBytesHex(send_block, 0, 20);
        }
        if (CRC16.compute(send_block, 0, 22, 0) != 0x0000B001) {
            throw new OneWireException("Second CRC didn't pass.");
        }
        if (DEBUG) {
            IOHelper.writeLine("next read:");
            IOHelper.writeBytesHex(send_block, 0, 22);
            IOHelper.writeLine("-------------------------------------------------------------");
        }
        System.arraycopy(send_block, 0, extra_info, extraStart, 20);
        return true;
    }
