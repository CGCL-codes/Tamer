    protected void zipDir(ZipOutputStream zos, File baseDir, String relativeSubDir) throws IOException {
        File dir = new File(baseDir, relativeSubDir);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                FileInputStream in = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(addFinalSeparator(relativeSubDir) + file.getName());
                entry.setTime(file.lastModified());
                entry.setSize(file.length());
                zos.putNextEntry(entry);
                copy(in, zos);
                zos.flush();
                zos.closeEntry();
                in.close();
            } else if (file.isDirectory()) {
                zipDir(zos, baseDir, addFinalSeparator(relativeSubDir) + file.getName());
            }
        }
    }
