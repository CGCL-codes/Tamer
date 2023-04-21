    byte[] getMAC(byte type, byte[] buf, int offset, int length) {
        byte[] byteArray = null;
        digest.update(macSecret, 0, macSecret.length);
        digest.update(PAD1, 0, padLength);
        byteArray = Utils.longToBytes(sequenceNumber);
        digest.update(byteArray, 0, byteArray.length);
        byteArray = new byte[3];
        byteArray[0] = type;
        byteArray[1] = (byte) (length >>> 8);
        byteArray[2] = (byte) (length & 0xff);
        digest.update(byteArray, 0, byteArray.length);
        byte[] innerHash = new byte[digest.getDigestLength()];
        digest.update(buf, offset, length);
        try {
            digest.digest(innerHash, 0, innerHash.length);
        } catch (DigestException e) {
        }
        digest.update(macSecret, 0, macSecret.length);
        digest.update(PAD2, 0, padLength);
        byte[] mac = new byte[innerHash.length];
        digest.update(innerHash, 0, innerHash.length);
        try {
            digest.digest(mac, 0, mac.length);
        } catch (DigestException e) {
        }
        return mac;
    }
