    public byte[] verifyLeaf(byte[] leafDigestValue, int position, String algorithm) {
        byte[] retDigestBytes = null;
        byte[] interimDigestBytes = LtansUtils.duplicateByteArray(leafDigestValue);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
        }
        int tmpPosition = position;
        int nodeNum = vector.size();
        if (nodeNum < 2) return null;
        for (int x = 0; x < nodeNum; x++) {
            DEROctetString derStr = (DEROctetString) vector.get(x);
            byte[] nodeBytes = derStr.getOctets();
            if ((tmpPosition % 2) == 0) {
                md.reset();
                md.update(interimDigestBytes);
                md.update(nodeBytes);
                interimDigestBytes = md.digest();
            } else if ((tmpPosition % 2) == 1) {
                md.reset();
                md.update(nodeBytes);
                md.update(interimDigestBytes);
                interimDigestBytes = md.digest();
            }
            tmpPosition = (int) Math.floor((double) (tmpPosition / 2));
        }
        return interimDigestBytes;
    }
