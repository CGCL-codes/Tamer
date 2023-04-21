    private int zip(ZipOutputStream zos, String fname) throws IOException {
        int ret;
        File file = getFile(fname);
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String f : files) {
                if ((ret = zip(zos, fname + "/" + f)) != 0) return ret;
            }
            return 0;
        }
        ZipEntry entry = new ZipEntry(fname);
        entry.setTime(file.lastModified());
        zos.putNextEntry(entry);
        FileInputStream fis = new FileInputStream(file);
        Util.copyStream(fis, zos);
        fis.close();
        zos.closeEntry();
        return 0;
    }
