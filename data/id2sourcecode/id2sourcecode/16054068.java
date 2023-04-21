    public static void download(File to, URL from) throws IOException {
        InputStream is = from.openStream();
        OutputStream os = new FileOutputStream(to);
        int i = 0;
        while ((i = is.read()) != -1) os.write(i);
        os.flush();
        is.close();
        os.close();
    }
