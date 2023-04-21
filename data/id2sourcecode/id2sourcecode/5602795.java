    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException {
        try {
            long address = TR[OFFSET_PLUS_BASE].getValue();
            Dinero din = Dinero.getInstance();
            din.Store(Converter.binToHex(Converter.positiveIntToBin(64, address)), 2);
            MemoryElement memEl = memory.getCell((int) address);
            memEl.writeHalf(TR[RT_FIELD].readHalf(0), (int) (address % 8));
            if (enableForwarding) {
                WB();
            }
        } catch (NotAlingException er) {
            throw new AddressErrorException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
