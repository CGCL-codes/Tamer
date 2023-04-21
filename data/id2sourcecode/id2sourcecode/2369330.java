    public static void makeStructZip(String zipURL, String xmlURL, String xmlFileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(zipURL));
        ZipOutputStream zos = new ZipOutputStream(fos);
        FileInputStream fis = new FileInputStream(xmlURL);
        zos.putNextEntry(new ZipEntry(xmlFileName + ".xml"));
        writeInOutputStream(fis, zos);
        zos.closeEntry();
        fis.close();
        zos.close();
    }
