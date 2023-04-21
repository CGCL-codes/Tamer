    public static void copyToWriter(Reader from, Writer to) throws Exception {
        char[] buffer = new char[BUFFER_SIZE];
        int chars_read;
        while (true) {
            chars_read = from.read(buffer);
            if (chars_read == -1) break;
            to.write(buffer, 0, chars_read);
        }
        to.flush();
        from.close();
    }
