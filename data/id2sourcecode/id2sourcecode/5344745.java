    private final void checkPLTE(final DataInputStream inputStream, final RepInfo repInfo, final int declChunkLen) throws IOException {
        if ((declChunkLen % 3) != 0) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Lunghezza PLTE non valida."));
        }
        paletteSize = 0;
        for (int i = 0; i < declChunkLen; i++) {
            int tmp = readUnsignedByte(inputStream, this);
            chcks.update((byte) tmp);
            tmp = readUnsignedByte(inputStream, this);
            chcks.update((byte) tmp);
            tmp = readUnsignedByte(inputStream, this);
            chcks.update((byte) tmp);
            paletteSize++;
        }
        if (paletteSize > maxPaletteSize) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Too many palette items in PLTE chunk"));
        }
        shortPalette = (paletteSize < maxPaletteSize);
        long crc32 = readUnsignedInt(inputStream, PNG_ENDIANITY, this);
        if (crc32 != chcks.getValue()) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("CRC Error in PLTE chunk"));
        }
    }
