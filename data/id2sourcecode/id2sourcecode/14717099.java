    public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            if (files.length == 0) {
                ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
                try {
                    zouts.putNextEntry(entry);
                    zouts.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    FileOperateUtils.zipFilesToZipFile(dirPath, files[i], zouts);
                } else {
                    FileOperateUtils.zipDirectoryToZipFile(dirPath, files[i], zouts);
                }
            }
        }
    }
