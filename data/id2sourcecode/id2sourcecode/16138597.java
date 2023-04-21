    private String readFull(DotTerminatedInputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while (-1 != (ch = in.read())) out.write(ch);
        return out.toString("US-ASCII");
    }
