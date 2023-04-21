    private byte[] computeChannelBinding(ChannelBinding channelBinding) throws GSSException {
        InetAddress initiatorAddress = channelBinding.getInitiatorAddress();
        InetAddress acceptorAddress = channelBinding.getAcceptorAddress();
        int size = 5 * 4;
        int initiatorAddressType = getAddrType(initiatorAddress);
        int acceptorAddressType = getAddrType(acceptorAddress);
        byte[] initiatorAddressBytes = null;
        if (initiatorAddress != null) {
            initiatorAddressBytes = getAddrBytes(initiatorAddress);
            size += initiatorAddressBytes.length;
        }
        byte[] acceptorAddressBytes = null;
        if (acceptorAddress != null) {
            acceptorAddressBytes = getAddrBytes(acceptorAddress);
            size += acceptorAddressBytes.length;
        }
        byte[] appDataBytes = channelBinding.getApplicationData();
        if (appDataBytes != null) {
            size += appDataBytes.length;
        }
        byte[] data = new byte[size];
        int pos = 0;
        writeLittleEndian(initiatorAddressType, data, pos);
        pos += 4;
        if (initiatorAddressBytes != null) {
            writeLittleEndian(initiatorAddressBytes.length, data, pos);
            pos += 4;
            System.arraycopy(initiatorAddressBytes, 0, data, pos, initiatorAddressBytes.length);
            pos += initiatorAddressBytes.length;
        } else {
            pos += 4;
        }
        writeLittleEndian(acceptorAddressType, data, pos);
        pos += 4;
        if (acceptorAddressBytes != null) {
            writeLittleEndian(acceptorAddressBytes.length, data, pos);
            pos += 4;
            System.arraycopy(acceptorAddressBytes, 0, data, pos, acceptorAddressBytes.length);
            pos += acceptorAddressBytes.length;
        } else {
            pos += 4;
        }
        if (appDataBytes != null) {
            writeLittleEndian(appDataBytes.length, data, pos);
            pos += 4;
            System.arraycopy(appDataBytes, 0, data, pos, appDataBytes.length);
            pos += appDataBytes.length;
        } else {
            pos += 4;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new GSSException(GSSException.FAILURE, -1, "Could not get MD5 Message Digest - " + e.getMessage());
        }
    }
