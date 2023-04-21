    public static void streamData(Reader reader, Writer writer) throws IOException {
        BufferedReader bufreader = new BufferedReader(reader);
        BufferedWriter bufwriter = new BufferedWriter(writer);
        final char[] buffer = new char[512];
        int length;
        while ((length = bufreader.read(buffer)) != -1) {
            bufwriter.write(buffer, 0, length);
        }
        bufwriter.flush();
    }
