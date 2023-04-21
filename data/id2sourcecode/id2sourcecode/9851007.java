    public void verify(byte[] key) throws InvalidKeyException {
        if (!isMasked() || payload == null) return;
        IMac m = getMac(key);
        m.update(payload, 0, payload.length - m.macSize());
        byte[] macValue = new byte[m.macSize()];
        System.arraycopy(payload, payload.length - macValue.length, macValue, 0, macValue.length);
        if (!Arrays.equals(macValue, m.digest())) throw new IllegalArgumentException("MAC verification failed");
        try {
            int len = payload.length - m.macSize();
            ByteArrayInputStream bais = new ByteArrayInputStream(payload, 0, len);
            DataInputStream in = new DataInputStream(bais);
            decodeEnvelope(in);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("malformed keyring fragment");
        }
        setMasked(false);
        payload = null;
    }
