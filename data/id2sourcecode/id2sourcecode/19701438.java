    protected void directCopy(InlineStringReader reader, InlineStringWriter writer) throws IOException {
        int bufSize = 1024;
        char[] buf = new char[bufSize];
        while (true) {
            int read = reader.read(buf);
            if (read == -1) break;
            writer.write(buf, 0, read);
        }
    }
