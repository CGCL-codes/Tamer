    public static String loadFile(File f) throws FileNotFoundException, IOException, UnsupportedEncodingException {
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        streamCopy(fis, baos);
        String res = new String(baos.toByteArray(), "ISO-8859-1");
        return res;
    }
