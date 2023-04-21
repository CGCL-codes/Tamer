    public UsernamePacket(Packet requestUsername, EthernetPacket ep, String Username, String Type) throws Exception {
        byte[] dstMac = new byte[6];
        arraycopy(requestUsername.header, 6, dstMac, 0, 6);
        ep.dst_mac = dstMac;
        this.datalink = ep;
        if (Type.charAt(0) == 'i') {
            fore = new byte[] { 0x01, 0x00, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x1a, 0x01 };
        } else {
            fore = new byte[] { 0x01, 0x00, 0x00, 0x15, 0x02, 0x00, 0x00, 0x15, 0x01 };
        }
        arraycopy(requestUsername.data, 5, fore, 5, 1);
        byte[] idAtType = new byte[Username.length() + "@".length() + Type.length()];
        idAtType = joinBytes(joinBytes(Username.getBytes(), "@".getBytes()), Type.getBytes());
        fore = joinBytes(fore, idAtType);
        byte[] last = "linkage".getBytes();
        last = joinBytes(last, new byte[] { 0x00 });
        byte[] secretKey = new byte[4];
        arraycopy(requestUsername.data, 0x15, secretKey, 0, 4);
        byte[] md5CodeTmp = "0123456789012345678901234567890123456789".getBytes();
        byte[] md5Code = joinBytes(md5CodeTmp, secretKey);
        md5Code = joinBytes(md5Code, new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(md5Code);
        byte[] md5Result = md5.digest();
        byte[] andByte = new byte[] { 0x34, (byte) 0xDE, (byte) 0xB6, 0x78, 0x00, (byte) 0xE7, 0x00, (byte) 0xE7 };
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) {
            result[i] = (byte) (andByte[i] ^ md5Result[i]);
        }
        byte[] packetData = joinBytes(fore, last);
        packetData = joinBytes(packetData, result);
        this.data = packetData;
    }
