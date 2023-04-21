    public static void autoPipeStream(InputStream i, OutputStream o) throws IOException {
        byte b[] = new byte[1024];
        int c = 0;
        while ((c = i.read(b)) > 0) o.write(b, 0, c);
        o.flush();
    }
