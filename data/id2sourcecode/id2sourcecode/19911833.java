    public static void copyAll(Reader reader, Writer writer) {
        try {
            char[] readBuffer = new char[READ_BUFFER_SIZE];
            int count;
            while ((count = reader.read(readBuffer)) != -1) {
                writer.write(readBuffer, 0, count);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
