    public void zipDirectory(String dir2zip, ZipOutputStream zos) throws Exception {
        File zipDir = new File(dir2zip);
        if (!zipDir.isDirectory()) throw new Exception("File [" + dir2zip + "] is not a directory.");
        String[] dirList = zipDir.list();
        if (dirList.length == 0) {
            zos.putNextEntry(new ZipEntry(getPath(dir2zip, _startDir2zip) + File.separator + "."));
        } else {
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDirectory(filePath, zos);
                    continue;
                }
                zipFile(f, zos);
            }
        }
    }
