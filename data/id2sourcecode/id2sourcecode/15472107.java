    public static void copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1024];
        int read;
        while ((read = input.read(buffer)) >= 0) {
            if (read == 0) {
                ThreadKit.sleep(50);
                continue;
            }
            output.write(buffer, 0, read);
        }
    }
