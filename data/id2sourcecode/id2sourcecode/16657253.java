    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException, IrregularWriteOperationException {
        try {
            long address = TR[OFFSET_PLUS_BASE].getValue();
            Dinero din = Dinero.getInstance();
            din.Store(Converter.binToHex(Converter.positiveIntToBin(64, address)), 4);
            MemoryElement memEl = memory.getCell((int) address);
            memEl.writeWord(TR[RT_FIELD].readWord(0), (int) (address % 8));
        } catch (NotAlingException er) {
            throw new AddressErrorException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
