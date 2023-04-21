    private static String is2String(InputStream is) {
        int c;
        int buffSize = 1024;
        byte buff[] = new byte[buffSize];
        OutputStream os = new ByteArrayOutputStream(buffSize);
        try {
            while ((c = is.read(buff)) != -1) os.write(buff, 0, c);
            return os.toString();
        } catch (IOException ex) {
            return null;
        }
    }
