    private void recursiveZipWrite(File file, ZipOutputStream out, String currentParentPath) throws IOException {
        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(currentParentPath + file.getName());
            entry.setTime(file.lastModified());
            out.putNextEntry(entry);
            FileInputStream in = new FileInputStream(file);
            StringUtils.copy(in, out);
            in.close();
            out.closeEntry();
        } else if (file.isDirectory()) {
            for (File subfile : file.listFiles()) {
                recursiveZipWrite(subfile, out, currentParentPath + file.getName() + "/");
            }
        }
    }
