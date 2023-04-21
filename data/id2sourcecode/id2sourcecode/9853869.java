    protected byte[] digest(String digest_alg, String alt_name) throws NoSuchAlgorithmException, IOException {
        int n;
        byte[] b;
        byte[] m;
        Iterator i;
        Map.Entry entry;
        Attributes attributes;
        InputStream in;
        MessageDigest md;
        m = new byte[alt_name.length()];
        for (n = 0; n < m.length; n++) {
            m[n] = (byte) alt_name.charAt(n);
        }
        b = new byte[0];
        md = MessageDigest.getInstance(digest_alg);
        for (i = entries_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            attributes = (Attributes) entry.getValue();
            in = getInputStream((String) entry.getKey());
            n = in.available();
            if (n > b.length) {
                b = new byte[n];
            }
            n = in.read(b);
            md.update(b, 0, n);
            md.update(m);
        }
        return md.digest();
    }
