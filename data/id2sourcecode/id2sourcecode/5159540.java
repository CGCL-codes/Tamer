    public static void compress(File directory, File zipfile, boolean remove) throws IOException, IllegalArgumentException {
        if (!directory.isDirectory()) throw new IllegalArgumentException("Not a directory:  " + directory);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        for (File f : FileIO.recursiveLs(directory)) {
            if (!f.isDirectory()) {
                FileInputStream in = new FileInputStream(f);
                URI path = directory.getCanonicalFile().toURI().relativize(f.getCanonicalFile().toURI());
                ZipEntry entry = new ZipEntry(path.getPath());
                out.putNextEntry(entry);
                appendInputStream(in, out);
            }
        }
        out.close();
        if (remove) FileIO.recursiveDelete(directory);
    }
