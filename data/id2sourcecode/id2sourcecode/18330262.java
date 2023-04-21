    private void marshal(Reference ref, String method, Object[] params) throws RemoteException {
        offset = 5;
        putShort(ref.getObjectID());
        String hashString = method;
        String hashModifier = ref.getHashModifier();
        if (hashModifier != null) {
            hashString = hashModifier + hashString;
        }
        byte[] buf = Utils.stringToBytes(hashString);
        SHA.reset();
        SHA.update(buf, 0, buf.length);
        try {
            SHA.digest(APDUBuffer, offset, SHA.getDigestLength());
        } catch (DigestException e) {
            throw new RemoteException("SHA1 error");
        }
        offset += 2;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object obj = params[i];
                if (obj == null) {
                    putByte(0xff);
                    continue;
                }
                if (obj instanceof Byte) {
                    putByte(((Byte) obj).byteValue());
                    continue;
                }
                if (obj instanceof Boolean) {
                    putByte(((Boolean) obj).booleanValue() ? 1 : 0);
                    continue;
                }
                if (obj instanceof Short) {
                    putShort(((Short) obj).shortValue());
                    continue;
                }
                if (obj instanceof Integer) {
                    putInt(((Integer) obj).intValue());
                    continue;
                }
                if (obj instanceof byte[]) {
                    byte[] param = (byte[]) obj;
                    putByte(param.length);
                    for (int k = 0; k < param.length; k++) {
                        putByte(param[k]);
                    }
                    continue;
                }
                if (obj instanceof boolean[]) {
                    boolean[] param = (boolean[]) obj;
                    putByte(param.length);
                    for (int k = 0; k < param.length; k++) {
                        putByte(param[k] ? 1 : 0);
                    }
                    continue;
                }
                if (obj instanceof short[]) {
                    short[] param = (short[]) obj;
                    putByte(param.length);
                    for (int k = 0; k < param.length; k++) {
                        putShort(param[k]);
                    }
                    continue;
                }
                if (obj instanceof int[]) {
                    int[] param = (int[]) obj;
                    putByte(param.length);
                    for (int k = 0; k < param.length; k++) {
                        putInt(param[k]);
                    }
                    continue;
                }
                throw new RemoteException("Incorrect parameter type");
            }
        }
        APDUBuffer[4] = (byte) (offset - 5);
        putByte(255);
    }
