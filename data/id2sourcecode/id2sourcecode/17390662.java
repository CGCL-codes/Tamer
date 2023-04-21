    private IOException copyRange(Reader reader, PrintWriter writer) {
        IOException exception = null;
        char buffer[] = new char[bufferInputSize];
        int len = buffer.length;
        while (true) {
            try {
                len = reader.read(buffer);
                if (len == -1) break;
                writer.write(buffer, 0, len);
            } catch (IOException e) {
                exception = e;
                len = -1;
                break;
            }
        }
        return exception;
    }
