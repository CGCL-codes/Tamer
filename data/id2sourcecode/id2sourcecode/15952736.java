    private void addDir(final ZipOutputStream out, final File parent, final File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                String name = null;
                if (parent != null) {
                    name = parent.getName() + ZipWriter.DIR_SEPARATOR + dir.getName();
                } else {
                    name = dir.getName();
                }
                ZipEntry entry = new ZipEntry(name);
                out.putNextEntry(entry);
                this.addDir(out, dir, f);
            } else if (f.isFile()) {
                this.addFile(out, dir, f);
            }
        }
    }
