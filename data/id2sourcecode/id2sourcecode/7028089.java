    private static void doFormat(BufferedReader reader, Writer writer, String indent) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            line = line.trim();
            if (containsStartToken(line)) {
                writer.write(indent + line + NEWLINE);
                doFormat(reader, writer, indent + INDENTCHAR);
            } else if (containsEndToken(line)) {
                indent = indent.substring(0, (indent.length() - 1));
                writer.write(indent + line + NEWLINE);
                return;
            } else {
                writer.write(indent + line + NEWLINE);
            }
            line = reader.readLine();
        }
    }
