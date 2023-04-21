    public byte[] changeDelta(byte[] oldKey, byte[] newKey, byte[] random) {
        MessageDigest hash = getDigestObject();
        int digestLength = hash.getDigestLength();
        if (logger.isDebugEnabled()) {
            logger.debug(protoName + "oldKey: " + new OctetString(oldKey).toHexString());
            logger.debug(protoName + "newKey: " + new OctetString(newKey).toHexString());
            logger.debug(protoName + "random: " + new OctetString(random).toHexString());
        }
        int iterations = (oldKey.length - 1) / hash.getDigestLength();
        OctetString tmp = new OctetString(oldKey);
        OctetString delta = new OctetString();
        for (int k = 0; k < iterations; k++) {
            tmp.append(random);
            hash.update(tmp.getValue());
            tmp.setValue(hash.digest());
            delta.append(new byte[digestLength]);
            for (int kk = 0; kk < digestLength; kk++) {
                delta.set(k * digestLength + kk, (byte) (tmp.get(kk) ^ newKey[k * digestLength + kk]));
            }
        }
        tmp.append(random);
        hash.update(tmp.getValue());
        tmp = new OctetString(hash.digest(), 0, oldKey.length - delta.length());
        for (int j = 0; j < tmp.length(); j++) {
            tmp.set(j, (byte) (tmp.get(j) ^ newKey[iterations * digestLength + j]));
        }
        byte[] keyChange = new byte[random.length + delta.length() + tmp.length()];
        System.arraycopy(random, 0, keyChange, 0, random.length);
        System.arraycopy(delta.getValue(), 0, keyChange, random.length, delta.length());
        System.arraycopy(tmp.getValue(), 0, keyChange, random.length + delta.length(), tmp.length());
        if (logger.isDebugEnabled()) {
            logger.debug(protoName + "keyChange:" + new OctetString(keyChange).toHexString());
        }
        return keyChange;
    }
