    public void authenticate(byte[] key) throws IOException, InvalidKeyException {
        if (isMasked()) throw new IllegalStateException("entry is masked");
        IMac m = getMac(key);
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        MacOutputStream macout = new MacOutputStream(bout, m);
        DataOutputStream out2 = new DataOutputStream(macout);
        for (Iterator it = entries.iterator(); it.hasNext(); ) {
            Entry entry = (Entry) it.next();
            entry.encode(out2);
        }
        bout.write(m.digest());
        payload = bout.toByteArray();
    }
