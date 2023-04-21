    public void lock() throws IOException, NoSuchAlgorithmException {
        ManifestDigester digester;
        OutputStream out;
        InputStream in;
        Map.Entry entry;
        Iterator i;
        String name;
        List list;
        Map dest;
        Map src;
        if (mf_ != null) {
            return;
        }
        if (preset_ == null) {
            in = struct_.getInputStream(AgentStructure.MANIFEST);
            if (in != null) {
                try {
                    preset_ = new Manifest(dAlgs_, dAlgs_, 0);
                    preset_.load(in);
                } finally {
                    in.close();
                }
            }
        }
        mf_ = new Manifest(dAlgs_, dAlgs_, 0);
        digester = new ManifestDigester(mf_);
        digester.digest(struct_);
        if (preset_ != null) {
            mf_.addAttributes(preset_.attributes(), true);
            src = preset_.entryMap();
            list = preset_.list(new StaticFilter());
            dest = mf_.entryMap();
            for (i = list.iterator(); i.hasNext(); ) {
                name = (String) i.next();
                dest.put(name, src.get(name));
            }
        }
        out = struct_.getOutputStream(AgentStructure.MANIFEST);
        if (out == null) {
            throw new IOException("Cannot get output stream for manifest!");
        }
        try {
            mf_.store(out);
        } finally {
            out.close();
        }
    }
