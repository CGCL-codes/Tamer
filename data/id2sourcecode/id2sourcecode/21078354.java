    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException, IrregularWriteOperationException {
        long address = TR[OFFSET_PLUS_BASE].getValue();
        Dinero din = Dinero.getInstance();
        din.Load(Converter.binToHex(Converter.positiveIntToBin(64, address)), 2);
        MemoryElement memEl = memory.getCell((int) address);
        try {
            TR[LMD_REGISTER].writeHalf(memEl.readHalf((int) (address % 8)));
            if (enableForwarding) {
                doWB();
            }
        } catch (NotAlingException er) {
            throw new AddressErrorException();
        }
    }
