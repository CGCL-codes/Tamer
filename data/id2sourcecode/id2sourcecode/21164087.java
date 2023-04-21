    private void zip(File root, File folder, ZipOutputStream zip) throws IOException {
        String path = root.getParentFile().getAbsolutePath();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String entryName = path.endsWith(File.separator) ? file.getAbsolutePath().substring(path.length()) : file.getAbsolutePath().substring(path.length() + 1);
                if (file.isFile()) {
                    ZipEntry entry = new ZipEntry(entryName);
                    zip.putNextEntry(entry);
                    InputStream in = new FileInputStream(file);
                    write(in, zip);
                    in.close();
                    zip.closeEntry();
                } else if (file.isDirectory()) {
                    ZipEntry entry = new ZipEntry(entryName + "/");
                    zip.putNextEntry(entry);
                    zip.closeEntry();
                    zip(root, file, zip);
                }
            }
        }
    }
