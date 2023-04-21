    public static MessageFormatAdapter parse(final String message) {
        Validate.notNull(message, "Cannot parse null message");
        final MessageFormatAdapter adapter = new MessageFormatAdapter();
        final BufferedReader reader = new BufferedReader(new StringReader(message));
        String line;
        do {
            try {
                line = StringUtils.defaultString(reader.readLine(), "");
            } catch (IOException io) {
                throw new IllegalStateException("IOException on reading in-memory string.");
            }
            final String[] pair = line.split("=", 2);
            if (pair.length == 2) {
                if ("null".equals(pair[1])) {
                    pair[1] = null;
                }
                adapter.parameters.put(pair[0], pair[1]);
            }
        } while (!"".equals(line));
        final StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(reader, writer);
        } catch (IOException io) {
            throw new IllegalStateException("IOException on reading in-memory string.");
        }
        adapter.body = writer.toString();
        return adapter;
    }
