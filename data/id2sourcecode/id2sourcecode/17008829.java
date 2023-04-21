    public void write(int startAddr, byte[] writeBuf, int offset, int len) throws OneWireIOException, OneWireException {
        if (len == 0) return;
        if (!ib.adapter.canDeliverPower()) throw new OneWireException("Power delivery required but not available");
        if ((startAddr + len) > PAGE_SIZE) throw new OneWireException("Write exceeds memory bank end");
        ib.doSpeed();
        if (!ib.adapter.select(ib.address)) throw new OneWireIOException("Device select failed");
        ib.adapter.putByte(WRITE_MEMORY_COMMAND);
        ib.adapter.putByte(startAddr & 0xFF);
        ib.adapter.dataBlock(writeBuf, offset, len);
        if (writeVerification) {
            byte[] read_buf = new byte[len];
            read(startAddr, true, read_buf, 0, len);
            for (int i = 0; i < len; i++) {
                if ((byte) read_buf[i] != (byte) writeBuf[i + offset]) throw new OneWireIOException("Read back from write compare is incorrect, page may be locked");
            }
        }
    }
