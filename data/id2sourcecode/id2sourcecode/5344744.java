    private final void checkIHDR(final DataInputStream inputStream, final RepInfo repInfo, final int declChunkLen) throws IOException {
        int tmp = (int) (readUnsignedInt(inputStream, PNG_ENDIANITY, this) & 0xFFFFFFFF);
        chcks.update(int2byteArray(tmp));
        if (tmp == 0) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal 0 value for height."));
        }
        tmp = (int) (readUnsignedInt(inputStream, PNG_ENDIANITY, this) & 0xFFFFFFFF);
        chcks.update(int2byteArray(tmp));
        if (tmp == 0) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal 0 value for width."));
        }
        tmp = readUnsignedByte(inputStream, this);
        chcks.update((byte) tmp);
        int colorType = readUnsignedByte(inputStream, this);
        chcks.update((byte) colorType);
        switch(colorType) {
            case 0:
                if (tmp != 1 && tmp != 2 && tmp != 4 && tmp != 8 && tmp != 16) {
                    repInfo.setValid(RepInfo.FALSE);
                    repInfo.setMessage(new ErrorMessage("In IHDR, illegal value for bit depth for colour type " + colorType + ": " + tmp));
                }
                repInfo.setProfile("PNG GrayScale");
                expectingPLTE = RepInfo.FALSE;
            case 3:
                if (tmp != 1 && tmp != 2 && tmp != 4 && tmp != 8) {
                    repInfo.setValid(RepInfo.FALSE);
                    repInfo.setMessage(new ErrorMessage("In IHDR, illegal value for bit depth for colour type " + colorType + ": " + tmp));
                }
                expectingPLTE = RepInfo.TRUE;
                colorDepth = tmp;
                maxPaletteSize = 1 << tmp;
                repInfo.setProfile("PNG Indexed");
                break;
            case 4:
                expectingPLTE = RepInfo.FALSE;
                if (tmp != 8 && tmp != 16) {
                    repInfo.setValid(RepInfo.FALSE);
                    repInfo.setMessage(new ErrorMessage("In IHDR, valore illegale per la profondita` dei bit per il colour type " + colorType + ": " + tmp));
                }
                repInfo.setProfile("PNG GrayScale with Alpha");
                break;
            case 6:
                expectingPLTE = RepInfo.FALSE;
                expecting_tRNS = RepInfo.FALSE;
                if (tmp != 8 && tmp != 16) {
                    repInfo.setValid(RepInfo.FALSE);
                    repInfo.setMessage(new ErrorMessage("In IHDR, valore illegale per la profondita` dei bit per il colour type " + colorType + ": " + tmp));
                }
                repInfo.setProfile("PNG Truecolor with Alpha");
                break;
            case 2:
                expectingPLTE = RepInfo.FALSE;
                expecting_tRNS = RepInfo.FALSE;
                if (tmp != 8 && tmp != 16) {
                    repInfo.setValid(RepInfo.FALSE);
                    repInfo.setMessage(new ErrorMessage("In IHDR, valore illegale per la profondita` dei bit per il colour type " + colorType + ": " + tmp));
                }
                repInfo.setProfile("PNG Truecolor");
                break;
            default:
                repInfo.setValid(RepInfo.FALSE);
                repInfo.setMessage(new ErrorMessage("In IHDR, valore illegale per il colour type (" + colorType + ")"));
                break;
        }
        tmp = readUnsignedByte(inputStream, this);
        chcks.update((byte) tmp);
        if (tmp != 0) {
            repInfo.setMessage(new InfoMessage("Attenzione, tipo di compressine " + tmp + " not conforme alla raccommandazione del W3C."));
        }
        tmp = readUnsignedByte(inputStream, this);
        chcks.update((byte) tmp);
        if (tmp != 0) {
            repInfo.setMessage(new InfoMessage("Attenzione, tipo di filtro " + tmp + " no ancora standardizzato dal W3C."));
        }
        tmp = readUnsignedByte(inputStream, this);
        chcks.update((byte) tmp);
        if (tmp != 0 && tmp != 1) {
            repInfo.setMessage(new InfoMessage("Attenzione, tipo di interlacciamento " + tmp + " no ancora standardizzato dal W3C."));
        }
        long crc32 = readUnsignedInt(inputStream, PNG_ENDIANITY, this);
        if (crc32 != chcks.getValue()) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Errore CRC nel chunk IHDR"));
        }
    }
