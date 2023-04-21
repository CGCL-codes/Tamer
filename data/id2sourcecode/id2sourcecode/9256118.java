    public void digest(String name, InputStream in) throws IOException {
        Attributes attr;
        String[] md;
        String s;
        byte[] b;
        int n;
        if (name == null || in == null) {
            throw new NullPointerException("name or input stream");
        }
        md = mf_.md_;
        try {
            digester_.digest(md, digests_, in);
        } catch (DigestException e) {
            throw new IllegalStateException("Caught " + e.getClass().getName() + "(\"" + e.getMessage() + "\")");
        }
        s = "";
        attr = new Attributes(digests_.size());
        for (n = digests_.size() - 1; n >= 0; n--) {
            b = (byte[]) digests_.get(md[n]);
            if (b != null) {
                attr.put(md[n] + "-Digest", Base64.encode(b));
                s = s + md[n] + " ";
            }
        }
        attr.put("Digest-Algorithms", s.trim());
        mf_.entries_.put(name, attr);
    }
