    private static void writeEntry(ZipOutputStream zos, String strFullPath, String strBaseDir) throws IOException {
        String strEntry = strFullPath.substring(strBaseDir.length());
        zos.putNextEntry(new ZipEntry(strEntry));
        FileInputStream fis = new FileInputStream(new File(strFullPath));
        IOUtil.copyStream(fis, zos);
        zos.closeEntry();
    }
