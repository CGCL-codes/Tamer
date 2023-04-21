    private final void checktEXT(final DataInputStream inputStream, final RepInfo repInfo, int declChunkLen) throws IOException {
        int c = -1;
        int keywordLen = 0;
        StringBuffer buf = new StringBuffer();
        while (keywordLen < MAX_KEYWORD_LEN) {
            c = readUnsignedByte(inputStream, this);
            chcks.update((byte) c);
            declChunkLen--;
            if (c == 0) {
                break;
            }
            keywordLen++;
            buf.append((char) c);
        }
        if (keywordLen == MAX_KEYWORD_LEN) {
            c = readUnsignedByte(inputStream, this);
            chcks.update((byte) c);
            declChunkLen--;
            if (c != 0) {
                repInfo.setValid(RepInfo.FALSE);
                repInfo.setMessage(new ErrorMessage("Missing 0 byte after keyword"));
                checkChunk(inputStream, repInfo, declChunkLen, "tEXT");
                buf.append((char) c);
            }
            return;
        }
        String keyword = buf.toString();
        buf.delete(0, buf.length());
        while (declChunkLen > 0) {
            c = readUnsignedByte(inputStream, this);
            chcks.update((byte) c);
            declChunkLen--;
        }
        String value = buf.toString();
        Property p = new Property(keyword, PropertyType.STRING, value);
        repInfo.setProperty(p);
        Booolean bol = (Booolean) keywordList.get(keyword);
        if (bol != null) {
            bol.setFlag(true);
        }
        long crc32 = readUnsignedInt(inputStream, PNG_ENDIANITY, this);
        if (crc32 != chcks.getValue()) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("CRC Error in tEXT chunk"));
        }
    }
