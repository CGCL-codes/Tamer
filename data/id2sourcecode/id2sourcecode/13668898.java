    private static boolean addEntry(File src, ZipOutputStream zip, String entry) throws IOException {
        if (!src.exists()) return false;
        zip.putNextEntry(new ZipEntry(entry));
        FileInputStream in = new FileInputStream(src);
        Streams.copyStream(in, zip);
        in.close();
        zip.closeEntry();
        return true;
    }
