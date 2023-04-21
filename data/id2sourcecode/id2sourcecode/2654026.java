    protected IOException copyRange(Reader reader, PrintWriter writer, long start, long end) {
        try {
            reader.skip(start);
        } catch (IOException e) {
            return e;
        }
        IOException exception = null;
        long bytesToRead = end - start + 1;
        char buffer[] = new char[input];
        int len = buffer.length;
        while ((bytesToRead > 0) && (len >= buffer.length)) {
            try {
                len = reader.read(buffer);
                if (bytesToRead >= len) {
                    writer.write(buffer, 0, len);
                    bytesToRead -= len;
                } else {
                    writer.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                exception = e;
                len = -1;
            }
            if (len < buffer.length) break;
        }
        return exception;
    }
