    public void process(Reader input, Writer output, Action action) throws ParsingException, IOException {
        BufferedReader reader = new BufferedReader(input);
        BufferedWriter writer = new BufferedWriter(output);
        process(reader, writer, action);
    }
