    private void getChannel1Msg(byte[] data, int index) throws ConvertStringException {
        RawData id1 = new RawData(data, index++, RawData.BYTE_LENGHT);
        RawData ver1 = new RawData(data, index++, RawData.BYTE_LENGHT);
        RawData frag1Lg = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData capabilities = new RawData(data, index, frag1Lg.getValue());
        RawData id2 = new RawData(data, index++, RawData.BYTE_LENGHT);
        RawData ver2 = new RawData(data, index++, RawData.BYTE_LENGHT);
        RawData frag2Lg = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        message = parseMessageType1(data, index, frag2Lg.getValue());
    }
