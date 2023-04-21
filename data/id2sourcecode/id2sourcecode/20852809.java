    public static byte[] readRequestResponse(SoldatInputStream in) throws IOException {
        in.readLine();
        in.readLine();
        in.skip(4L);
        byte[] buf = new byte[1000];
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        int read;
        while ((read = in.read(buf)) != -1) {
            tmp.write(buf, 0, read);
        }
        ByteArrayOutputStream tmp2 = new ByteArrayOutputStream();
        tmp2.write(tmp.toByteArray(), 0, tmp.size() - ("ENDFILES" + endOfLine).length());
        return tmp2.toByteArray();
    }
