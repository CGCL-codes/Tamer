    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException {
        try {
            long address = TR[OFFSET_PLUS_BASE].getValue();
            Dinero din = Dinero.getInstance();
            din.Store(Converter.binToHex(Converter.positiveIntToBin(64, address)), 1);
            MemoryElement memEl = memory.getCell((int) address);
            memEl.writeByte(TR[RT_FIELD].readByte(0), (int) (address % 8));
            if (enableForwarding) {
                WB();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
