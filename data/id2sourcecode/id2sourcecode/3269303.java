    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException, IrregularWriteOperationException {
        long address = TR[OFFSET_PLUS_BASE].getValue();
        Dinero din = Dinero.getInstance();
        din.Load(Converter.binToHex(Converter.positiveIntToBin(64, address)), 1);
        MemoryElement memEl = memory.getCell((int) address);
        int read = memEl.readByteUnsigned((int) (address % 8));
        edumips64.Main.logger.debug("LBU: read from address " + address + " the value " + read);
        TR[LMD_REGISTER].writeByteUnsigned(read);
        if (enableForwarding) {
            doWB();
        }
    }
