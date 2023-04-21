    public MessageAutoReply__4_11(byte[] array) throws Exception {
        super(array, true);
        byte data[] = getSnac().getDataFieldByteArray();
        int index = 0;
        RawData id = new RawData(data, index, 2 * RawData.DWORD_LENGHT);
        index += 2 * RawData.DWORD_LENGHT;
        messageChannel = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        RawData uinLg = new RawData(data, index, RawData.BYTE_LENGHT);
        index += RawData.BYTE_LENGHT;
        senderID = new String(data, index, uinLg.getValue());
        index += uinLg.getValue();
        RawData reasonCode = new RawData(data, index, RawData.WORD_LENGHT);
        index += RawData.WORD_LENGHT;
        if (reasonCode.getValue() == CHANNEL_SPECIFIC) {
            switch(messageChannel.getValue()) {
                case 1:
                    getChannel1Msg(data, index);
                    break;
                case 2:
                    getChannel2Msg(data, index);
                    break;
                default:
                    throw new Exception("Unknown channel");
            }
        }
        if (messageChannel.getValue() == 2 && messageType.getType() == MessageTypeEnum.XSTATUS_MESSAGE) {
            isResponseXStatus = true;
        }
    }
