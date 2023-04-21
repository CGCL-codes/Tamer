    private final void checktIME(final DataInputStream inputStream, final RepInfo repInfo, final int declChunkLen) throws IOException {
        int yhrHigh = readUnsignedByte(inputStream, this);
        chcks.update((byte) yhrHigh);
        int yhrLow = readUnsignedByte(inputStream, this);
        chcks.update((byte) yhrLow);
        int month = readUnsignedByte(inputStream, this);
        chcks.update((byte) month);
        if (month < 1 || month > 12) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal month value in tIME chunk"));
        }
        int day = readUnsignedByte(inputStream, this);
        chcks.update((byte) day);
        if (day < 1 || day > 31) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal day value in tIME chunk"));
        }
        int hour = readUnsignedByte(inputStream, this);
        chcks.update((byte) hour);
        if (hour < 0 || hour > 23) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal hour value in tIME chunk"));
        }
        int minute = readUnsignedByte(inputStream, this);
        chcks.update((byte) minute);
        if (minute < 0 || minute > 59) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal minute value in tIME chunk"));
        }
        int second = readUnsignedByte(inputStream, this);
        chcks.update((byte) second);
        if (second < 0 || second > 60) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("Illegal second value in tIME chunk"));
        }
        long crc32 = readUnsignedInt(inputStream, PNG_ENDIANITY, this);
        if (crc32 != chcks.getValue()) {
            repInfo.setValid(RepInfo.FALSE);
            repInfo.setMessage(new ErrorMessage("CRC Error in tIME chunk"));
        }
        second = (second == 60) ? 59 : second;
        month--;
        repInfo.setLastModified(new GregorianCalendar((yhrHigh << 8) + yhrLow, month, day, hour, minute, second).getTime());
    }
