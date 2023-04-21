    ImagePlus openZipUsingUrl(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        InputStream in = uc.getInputStream();
        ZipInputStream zis = new ZipInputStream(in);
        ZipEntry entry = zis.getNextEntry();
        if (entry == null) return null;
        String name = entry.getName();
        if (!(name.endsWith(".tif") || name.endsWith(".dcm"))) throw new IOException("This ZIP archive does not appear to contain a .tif or .dcm file\n" + name);
        if (name.endsWith(".dcm")) return openDicomStack(zis, entry); else return openTiff(zis, name);
    }
