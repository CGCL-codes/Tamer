    public static final void download(URL url, String newLocal) throws FileNotFoundException, IOException {
        InputStream in = url.openStream();
        byte[] buffer = Utils.loadBytes(in);
        OutputStream out = new FileOutputStream(newLocal);
        out.write(buffer);
        out.flush();
        in.close();
        out.close();
    }
