    public boolean save(File file) {
        ObjectOutputStream oos;
        ZipOutputStream zos;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            zos.putNextEntry(new ZipEntry("dataspace"));
            zos.setLevel(9);
            oos = new ObjectOutputStream(zos);
            save(oos);
            oos.flush();
            oos.close();
            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
