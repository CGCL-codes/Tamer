    private void getChannel2Msg(byte[] data, int index) throws ConvertStringException {
        RawData part1Lg = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData protocolVersion = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData guid = new RawData(data, index, 16);
        index += 16;
        RawData unknownWord = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData clientCapabilities = new RawData(data, index, RawData.DWORD_LENGHT);
        index += RawData.DWORD_LENGHT;
        RawData unknownByte = new RawData(data, index, RawData.BYTE_LENGHT);
        index += RawData.BYTE_LENGHT;
        RawData cntr1 = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData part2Lg = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData cntr2 = new RawData(data, index, RawData.WORD_LENGHT);
        part2Lg.invertIndianness();
        index += part2Lg.getValue();
        message = parseMessageType2(data, index);
    }
