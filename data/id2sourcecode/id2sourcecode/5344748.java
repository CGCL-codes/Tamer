    private final void checkChunk(final DataInputStream inputStream, final RepInfo repInfo, int declChunkLen, final String chunkSig) throws IOException {
        int read = 0;
        while (read < declChunkLen) {
            int tmp = readUnsignedByte(inputStream, this);
            chcks.update((byte) tmp);
            read++;
        }
        long crc32 = readUnsignedInt(inputStream, PNG_ENDIANITY, this);
        if (crc32 != chcks.getValue()) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("CRC Error in " + chunkSig + " chunk"));
        }
    }
