    private static File copyFromURL(URL url, String dir) throws IOException {
        File urlFile = new File(url.getFile());
        File dest = new File(dir, urlFile.getName());
        FileOutputStream os = new FileOutputStream(dest);
        InputStream is = url.openStream();
        byte data[] = new byte[4096];
        int ct;
        while ((ct = is.read(data)) >= 0) os.write(data, 0, ct);
        is.close();
        os.close();
        return dest;
    }
