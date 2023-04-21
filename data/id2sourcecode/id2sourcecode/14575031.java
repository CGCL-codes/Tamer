    public void save(File file) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        for (File f : this.files) {
            if (f.isDirectory()) {
                ZipEntry entry = new ZipEntry(f.getName() + ZipWriter.SEPARATOR);
                out.putNextEntry(entry);
                out.closeEntry();
                ZipWriter.addDir(out, f);
            } else if (f.isFile()) {
                ZipWriter.addFile(out, f);
            }
        }
        out.close();
    }
