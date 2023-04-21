    private void addDirEntry(String[] fileList, String path, String rpath) throws Exception {
        int i;
        File fin;
        String[] subfilelist;
        ZipEntry entry;
        String fname, zentry;
        for (i = 0; i < fileList.length; i++) {
            if (fileList[i].equalsIgnoreCase("ZipStore")) continue;
            fname = path + "/" + fileList[i];
            if (rpath == null) {
                zentry = fileList[i];
            } else {
                zentry = rpath + fileList[i];
            }
            fin = new File(fname);
            if (fin.isDirectory()) {
                subfilelist = fin.list();
                addDirEntry(subfilelist, fname, zentry + "/");
            } else {
                if (!(zentry.endsWith("odt"))) {
                    entry = new ZipEntry(zentry);
                    zipf.putNextEntry(entry);
                    addFileEntry(new FileInputStream(fin));
                    zipf.closeEntry();
                }
            }
        }
    }
