    public void sendSmd(Socket sock, SensorMetaData smd) {
        String id = smd.getId();
        short len = (short) id.length();
        short numChannels = (short) smd.getNumChannels();
        Vector datatypes = smd.getChannelDatatypes();
        byte[] idLenArr = Utility.shortToByteArray(len);
        byte[] numChannelsArr = Utility.shortToByteArray(numChannels);
        byte[] idArr = id.getBytes();
        byte[] finalBuff = new byte[idLenArr.length + numChannelsArr.length + idArr.length + idLenArr.length * datatypes.size()];
        int offset = 0;
        System.arraycopy(idLenArr, 0, finalBuff, offset, idLenArr.length);
        offset += idLenArr.length;
        System.arraycopy(idArr, 0, finalBuff, offset, idArr.length);
        offset += idArr.length;
        System.arraycopy(numChannelsArr, 0, finalBuff, offset, numChannelsArr.length);
        offset += numChannelsArr.length;
        for (int i = 0; i < datatypes.size(); i++) {
            byte[] dtArr = Utility.shortToByteArray((short) (((Integer) datatypes.elementAt(i)).intValue()));
            System.arraycopy(dtArr, 0, finalBuff, offset, dtArr.length);
            offset += dtArr.length;
        }
        send(sock, finalBuff);
    }
