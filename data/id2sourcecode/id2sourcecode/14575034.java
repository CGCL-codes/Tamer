    private static void addDir(ZipOutputStream out, File parent, File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                String name = (parent != null ? parent.getName() + ZipWriter.SEPARATOR : ZipWriter.BLANK) + dir.getName() + ZipWriter.SEPARATOR;
                out.putNextEntry(new ZipEntry(name));
                out.closeEntry();
                ZipWriter.addDir(out, dir, f);
            } else if (f.isFile()) {
                ZipWriter.addFile(out, dir, f);
            }
        }
    }
