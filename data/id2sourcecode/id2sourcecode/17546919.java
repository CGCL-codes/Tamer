    public static void unzip(ZipInputStream zipinputstream, String s, String s1) throws IOException {
        if (s.endsWith("/")) {
            File file = (new File(s1 + File.separator + s + "dummy")).getParentFile();
            file.mkdirs();
            return;
        }
        FileOutputStream fileoutputstream = new FileOutputStream(s1 + File.separator + s);
        byte abyte0[] = new byte[512];
        for (int i = 0; (i = zipinputstream.read(abyte0)) != -1; ) fileoutputstream.write(abyte0, 0, i);
        fileoutputstream.close();
    }
