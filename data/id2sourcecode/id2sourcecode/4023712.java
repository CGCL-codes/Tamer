    public static void get(URL url, OutputStream os) throws IOException {
        InputStream is = url.openStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int ch;
        while ((ch = bis.read()) != -1) bos.write(ch);
        bis.close();
        is.close();
        bos.close();
    }
