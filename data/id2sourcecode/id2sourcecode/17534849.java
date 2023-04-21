    protected static void writeAdvertisement(File f, Advertisement adv) throws IOException, FileNotFoundException {
        InputStream in = adv.getDocument(new MimeMediaType("text/xml")).getStream();
        FileOutputStream out = new FileOutputStream(f);
        byte[] buf = new byte[8192];
        int r;
        while ((r = in.read(buf)) > 0) out.write(buf, 0, r);
        out.close();
    }
