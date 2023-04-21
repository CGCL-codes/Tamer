    private void extractZip(InputStream in, File todir) throws IOException, FileNotFoundException {
        File z = new File(todir, "newzip.zip");
        FileOutputStream out = new FileOutputStream(z);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        out.close();
        ZipFile zf = new ZipFile(z);
        Tools.unpackZip(zf, todir);
    }
