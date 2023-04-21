    private void setUpRealConsoleReader() throws IOException {
        writer = new StringWriter();
        in = new ByteArrayInputStream(new byte[] {});
        reader = new ConsoleReader(in, writer);
        console = new JLineConsole(reader);
    }
