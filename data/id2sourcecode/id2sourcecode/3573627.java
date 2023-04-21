    public void sendSmd(Socket sock, SensorMetaData smd) {
        String id = smd.getId();
        short len = (short) id.length();
        short numChannels = (short) (smd.getNumChannels() - 1);
        Vector datatypes = smd.getChannelDatatypes();
        byte[] idLenArr = Utility.shortToByteArray(len);
        byte[] numChannelsArr = Utility.shortToByteArray(numChannels);
        byte[] idArr = id.getBytes();
        String webSerStr = smd.getWebServiceString();
        short webSerStrLen = (short) webSerStr.length();
        byte[] webSerStrLenArr = Utility.shortToByteArray(webSerStrLen);
        byte[] webSerStrArr = webSerStr.getBytes();
        byte[] finalBuff = new byte[idLenArr.length + numChannelsArr.length + idArr.length + idLenArr.length * datatypes.size() + webSerStrLenArr.length + webSerStrArr.length];
        int offset = 0;
        System.arraycopy(idLenArr, 0, finalBuff, offset, idLenArr.length);
        offset += idLenArr.length;
        System.arraycopy(idArr, 0, finalBuff, offset, idArr.length);
        offset += idArr.length;
        System.arraycopy(numChannelsArr, 0, finalBuff, offset, numChannelsArr.length);
        offset += numChannelsArr.length;
        for (int i = 1; i < datatypes.size(); i++) {
            byte[] dtArr = Utility.shortToByteArray((short) (((Integer) datatypes.elementAt(i)).intValue()));
            System.arraycopy(dtArr, 0, finalBuff, offset, dtArr.length);
            offset += dtArr.length;
        }
        System.arraycopy(webSerStrLenArr, 0, finalBuff, offset, webSerStrLenArr.length);
        offset += webSerStrLenArr.length;
        System.arraycopy(webSerStrArr, 0, finalBuff, offset, webSerStrArr.length);
        offset += webSerStrArr.length;
        send(sock, finalBuff);
    }
