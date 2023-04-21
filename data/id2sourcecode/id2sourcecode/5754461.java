    private List extractFilesFromZip(byte[] zipData) throws Exception {
        ByteArrayInputStream bis = null;
        ZipInputStream zipinputstream = null;
        try {
            bis = new ByteArrayInputStream(zipData);
            zipinputstream = new ZipInputStream(bis);
            byte[] buf = new byte[1024];
            List files = new ArrayList();
            ZipEntry zipentry = null;
            do {
                zipentry = zipinputstream.getNextEntry();
                if (zipentry != null) {
                    String entryName = zipentry.getName();
                    int n;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(Integer.parseInt(Long.toString(zipentry.getSize())));
                    FileJasper newFile = new FileJasper();
                    newFile.setFileName(entryName);
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) bos.write(buf, 0, n);
                    newFile.setData(bos.toByteArray());
                    bos.close();
                    zipinputstream.closeEntry();
                    files.add(newFile);
                }
            } while (zipentry != null);
            return files;
        } catch (Exception ex) {
            throw new Exception("Excepcion al extraer ficheros del zip de la plantilla jasper", ex);
        } finally {
            try {
                bis.close();
            } catch (Exception ex) {
            }
            try {
                zipinputstream.close();
            } catch (Exception ex) {
            }
        }
    }
