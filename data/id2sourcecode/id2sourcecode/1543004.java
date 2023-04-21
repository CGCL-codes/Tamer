    public static String convert(File file) throws IOException, UnsupportedEncodingException {
        InputStream is = new FileInputStream(file);
        Reader ir = new InputStreamReader(is, "utf-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(62 * 1024);
        Writer w = new OutputStreamWriter(baos, "utf-8");
        convert(ir, w);
        try {
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = new String(baos.toByteArray());
        return (s);
    }
