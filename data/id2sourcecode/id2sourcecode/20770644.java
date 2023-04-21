    public byte[] getBytes(ShortMessage message) {
        byte[] data = new byte[4];
        data[0] = (byte) message.getCommand();
        data[1] = (byte) message.getChannel();
        data[2] = (byte) message.getData1();
        data[3] = (byte) message.getData2();
        return data;
    }
