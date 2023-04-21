    protected void readReply(BufferedReader di, Writer writer) throws IOException {
        String line;
        while (true) {
            line = di.readLine();
            if (line == null) throw new EOFException();
            writer.write(line);
            writer.write(CRLF);
            if (line.length() <= 3 || line.charAt(3) != '-') {
                break;
            }
        }
        writer.flush();
    }
