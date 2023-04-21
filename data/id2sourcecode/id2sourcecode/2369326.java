    public static void makeZip(String zipURL, String xmlURL, String xmlFileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(zipURL));
        ZipOutputStream zos = new ZipOutputStream(fos);
        FileInputStream fis = new FileInputStream(xmlURL);
        zos.putNextEntry(new ZipEntry(xmlFileName + ".xml"));
        writeInOutputStream(fis, zos);
        String bpath = PathHandler.getBLobFilePathForClient();
        FileInputStream fisBLOB = createInputStream(bpath);
        zos.putNextEntry(new ZipEntry("blob.lob"));
        writeInOutputStream(fisBLOB, zos);
        zos.closeEntry();
        String cpath = PathHandler.getCLobFilePathForClient();
        FileInputStream fisCLOB = createInputStream(cpath);
        zos.putNextEntry(new ZipEntry("clob.lob"));
        writeInOutputStream(fisCLOB, zos);
        zos.closeEntry();
        fis.close();
        fisCLOB.close();
        fisBLOB.close();
        zos.close();
        fos.close();
    }
