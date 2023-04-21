    public void close() throws IOException {
        OutputStream m = sos.getOutputStream(), o;
        sos.last = false;
        super.close();
        if (sig != null) sig.writeSignatures(m);
        o = pos.getOutputStream();
        pos.last = false;
        m.close();
        o.write(0xD3);
        o.write(md.getDigestLength());
        o.write(md.digest());
        o.close();
        o = null;
        m = null;
        out = null;
        pos = null;
        sos = null;
        md = null;
    }
