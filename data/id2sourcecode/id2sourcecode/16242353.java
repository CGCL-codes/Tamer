    @Override
    public boolean execute(final String cmd, final OutputStream o) throws IOException {
        out.print(cmd);
        out.write(0);
        final BufferInput bi = new BufferInput(in);
        int l;
        while ((l = bi.read()) != 0) o.write(l);
        info = bi.readString();
        return bi.read() == 0;
    }
