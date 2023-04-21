    private void processUpdateBinary(APDU apdu) {
        if (!hasFileSelected() || isLocked()) {
            ISOException.throwIt(SW_CONDITIONS_NOT_SATISFIED);
        }
        byte[] buffer = apdu.getBuffer();
        byte p1 = buffer[OFFSET_P1];
        byte p2 = buffer[OFFSET_P2];
        short offset = Util.makeShort(p1, p2);
        short readCount = (short) (buffer[ISO7816.OFFSET_LC] & 0xff);
        readCount = apdu.setIncomingAndReceive();
        while (readCount > 0) {
            fileSystem.writeData(selectedFile, offset, buffer, OFFSET_CDATA, readCount);
            offset += readCount;
            readCount = apdu.receiveBytes(ISO7816.OFFSET_CDATA);
        }
    }
