    protected boolean preProcessDirectory(File file, Path path) {
        try {
            ZipEntry ze = new ZipEntry(path + "/");
            ze.setTime(file.lastModified());
            zout.putNextEntry(ze);
            zout.closeEntry();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
