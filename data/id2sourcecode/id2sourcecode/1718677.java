    public static void copyInput2Output(InputStream in, OutputStream out) throws IOException {
        int read = 0;
        byte buffer[] = new byte[8192];
        while ((read = in.read(buffer)) > -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
    }
