    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeInt(conference);
        out.writeInt(lastread);
    }
