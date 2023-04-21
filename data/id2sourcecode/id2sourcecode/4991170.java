    public static void zipFile(String strBaseDir, String strInput, String strOutputDir, String strOutputFile) throws IOException {
        strBaseDir = sanitizeDirectoryPath(strBaseDir);
        strOutputDir = sanitizeDirectoryPath(strOutputDir);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(strOutputDir, strOutputFile)));
        ZipEntry ze = new ZipEntry(stripLeadingSlash(strInput));
        zos.putNextEntry(ze);
        File fileSource = new File(strBaseDir, strInput);
        DataInputStream dis = new DataInputStream(new FileInputStream(fileSource));
        IOUtil.copyStream(dis, zos);
        zos.close();
        dis.close();
    }
