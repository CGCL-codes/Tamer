    public static byte[] computeHMAC(HipPacket packet, HostIDParameter hi, DigestAlgo algo) {
        int length = 0;
        int offset = packet.__offset + HIP_COMMN_HEADER_LEN;
        boolean isHMAC2 = false;
        byte[] hmacData;
        HipParameter param = null;
        length = packet.getHIPLength();
        packet.setHIPChecksum(0);
        while ((param = packet.getNextParameter(offset)) != null) {
            if (param instanceof HMAC2Parameter) {
                offset += hi.size();
                isHMAC2 = true;
                break;
            } else if (param instanceof HMACParameter) {
                break;
            }
            offset += param.size();
        }
        packet.setHIPLength((byte) ((offset - 8 - packet.__offset) / 8));
        hmacData = new byte[offset - packet.__offset];
        System.arraycopy(packet._data_, packet.__offset, hmacData, 0, offset - packet.__offset - (isHMAC2 ? hi.size() : 0));
        if (isHMAC2) hi.getData(hmacData, hmacData.length - hi.size(), HipParameter.PARAM_HEADER_OFFSET + hi.getLength());
        packet.setHIPLength((byte) length);
        return algo.digest(hmacData);
    }
