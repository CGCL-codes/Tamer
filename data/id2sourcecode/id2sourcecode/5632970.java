    public static void encrypt(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream tmpout = new ByteArrayOutputStream();
        encrypt(in, tmpout);
        in.close();
        FileOutputStream out = new FileOutputStream(file);
        ByteArrayInputStream src = new ByteArrayInputStream(tmpout.toByteArray());
        StreamUtils.transfer(src, out);
        out.close();
    }
