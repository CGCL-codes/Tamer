    public void verify(char[] password) {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "verify");
        if (isMasked() && payload != null) {
            if (Configuration.DEBUG) log.fine("payload to verify: " + Util.dumpString(payload));
            long tt = -System.currentTimeMillis();
            IMac m = null;
            try {
                m = getMac(password);
            } catch (Exception x) {
                throw new IllegalArgumentException(x.toString(), x);
            }
            int limit = payload.length - m.macSize();
            m.update(payload, 0, limit);
            byte[] macValue = new byte[m.macSize()];
            System.arraycopy(payload, payload.length - macValue.length, macValue, 0, macValue.length);
            if (!Arrays.equals(macValue, m.digest())) throw new IllegalArgumentException("MAC verification failed");
            setMasked(false);
            ByteArrayInputStream bais;
            try {
                bais = new ByteArrayInputStream(payload, 0, limit);
                DataInputStream in = new DataInputStream(bais);
                decodeEnvelope(in);
            } catch (IOException ioe) {
                throw new IllegalArgumentException("malformed keyring fragment");
            }
            tt += System.currentTimeMillis();
            if (Configuration.DEBUG) log.fine("Verified in " + tt + "ms.");
        } else if (Configuration.DEBUG) log.fine("Skip verification; " + (isMasked() ? "null payload" : "unmasked"));
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "verify");
    }
