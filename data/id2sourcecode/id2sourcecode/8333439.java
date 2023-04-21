    protected void computeExchangeHash_H() {
        SSH2DataBuffer buf = new SSH2DataBuffer(64 * 1024);
        if (transport.isServer()) {
            serverF = dhPublicKey.getY();
        } else {
            clientE = dhPublicKey.getY();
        }
        buf.writeString(transport.getClientVersion());
        buf.writeString(transport.getServerVersion());
        buf.writeString(transport.getClientKEXINITPDU().getData(), transport.getClientKEXINITPDU().getPayloadOffset(), transport.getClientKEXINITPDU().getPayloadLength());
        buf.writeString(transport.getServerKEXINITPDU().getData(), transport.getServerKEXINITPDU().getPayloadOffset(), transport.getServerKEXINITPDU().getPayloadLength());
        buf.writeString(serverHostKey);
        buf.writeBigInt(clientE);
        buf.writeBigInt(serverF);
        buf.writeString(sharedSecret_K);
        sha1.reset();
        sha1.update(buf.getData(), 0, buf.getWPos());
        exchangeHash_H = sha1.digest();
        transport.getLog().debug2("SSH2KEXDHGroup1SHA1", "computeExchangeHash_H", "E: ", clientE.toByteArray());
        transport.getLog().debug2("SSH2KEXDHGroup1SHA1", "computeExchangeHash_H", "F: ", serverF.toByteArray());
        transport.getLog().debug2("SSH2KEXDHGroup1SHA1", "computeExchangeHash_H", "K: ", sharedSecret_K);
        transport.getLog().debug2("SSH2KEXDHGroup1SHA1", "computeExchangeHash_H", "Hash over: ", buf.getData(), 0, buf.getWPos());
        transport.getLog().debug2("SSH2KEXDHGroup1SHA1", "computeExchangeHash_H", "H: ", exchangeHash_H);
    }
