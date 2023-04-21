    public void preScan(File dir, DirectoryScanner scanner) throws IOException {
        sink_.putNextEntry(new ZipEntry(scanner.path_.toString().replace(File.separatorChar, '/') + "/"));
        sink_.closeEntry();
    }
