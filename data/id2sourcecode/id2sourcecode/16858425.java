    public static PasswordAuthenticatedEntry decode(DataInputStream in, char[] password) throws IOException {
        PasswordAuthenticatedEntry entry = new PasswordAuthenticatedEntry();
        entry.properties.decode(in);
        IMac mac = entry.getMac(password);
        int len = in.readInt() - mac.macSize();
        MeteredInputStream min = new MeteredInputStream(in, len);
        MacInputStream macin = new MacInputStream(min, mac);
        DataInputStream in2 = new DataInputStream(macin);
        entry.setMasked(false);
        entry.decodeEnvelope(in2);
        byte[] macValue = new byte[mac.macSize()];
        in.readFully(macValue);
        if (!Arrays.equals(macValue, mac.digest())) throw new MalformedKeyringException("MAC verification failed");
        return entry;
    }
