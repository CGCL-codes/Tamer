    public static void WriteZipEntry(String fileName, String entryPrefix, ZipOutputStream zout) throws Exception {
        if ((fileName == "") || (entryPrefix == "") || (zout == null)) return;
        File layerFile = new File(fileName);
        if (layerFile.exists()) {
            int dotPosition = fileName.lastIndexOf('.');
            String ext = fileName.substring(dotPosition);
            zout.putNextEntry(new ZipEntry(entryPrefix + ext));
            FileInputStream fin = new FileInputStream(fileName);
            writeToZip(fin, zout);
            if (ext.toLowerCase().equalsIgnoreCase(".shp")) {
                fileName = layerFile.getParent() + File.separator + GUIUtil.nameWithoutExtension(layerFile);
                if (new File(fileName + ".shx").exists()) {
                    zout.putNextEntry(new ZipEntry(entryPrefix + ".shx"));
                    fin = new FileInputStream(fileName + ".shx");
                    writeToZip(fin, zout);
                }
                if (new File(fileName + ".dbf").exists()) {
                    zout.putNextEntry(new ZipEntry(entryPrefix + ".dbf"));
                    fin = new FileInputStream(fileName + ".dbf");
                    writeToZip(fin, zout);
                }
                if (new File(fileName + ".shp.xml").exists()) {
                    zout.putNextEntry(new ZipEntry(entryPrefix + ".shp.xml"));
                    fin = new FileInputStream(fileName + ".shp.xml");
                    writeToZip(fin, zout);
                }
                if (new File(fileName + ".prj").exists()) {
                    zout.putNextEntry(new ZipEntry(entryPrefix + ".prj"));
                    fin = new FileInputStream(fileName + ".prj");
                    writeToZip(fin, zout);
                }
            }
        }
    }
