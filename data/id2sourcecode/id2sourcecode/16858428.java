    public void authenticate(char[] password) throws IOException {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "authenticate");
        long tt = -System.currentTimeMillis();
        long t1 = -System.currentTimeMillis();
        if (isMasked()) throw new IllegalStateException("entry is masked");
        byte[] salt = new byte[8];
        PRNG.getInstance().nextBytes(salt);
        t1 += System.currentTimeMillis();
        if (Configuration.DEBUG) log.fine("-- Generated salt in " + t1 + "ms.");
        properties.put("salt", Util.toString(salt));
        IMac m = getMac(password);
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        MacOutputStream macout = new MacOutputStream(bout, m);
        DataOutputStream out2 = new DataOutputStream(macout);
        for (Iterator it = entries.iterator(); it.hasNext(); ) {
            Entry entry = (Entry) it.next();
            if (Configuration.DEBUG) log.fine("-- About to authenticate one " + entry);
            t1 = -System.currentTimeMillis();
            entry.encode(out2);
            t1 += System.currentTimeMillis();
            if (Configuration.DEBUG) log.fine("-- Authenticated an Entry in " + t1 + "ms.");
        }
        bout.write(m.digest());
        payload = bout.toByteArray();
        if (Configuration.DEBUG) log.fine("authenticated payload: " + Util.dumpString(payload));
        setMasked(true);
        tt += System.currentTimeMillis();
        if (Configuration.DEBUG) {
            log.fine("Authenticated in " + tt + "ms.");
            log.exiting(this.getClass().getName(), "authenticate");
        }
    }
