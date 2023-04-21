    public void exportFile(String path, String fileName, String exportType, WQXExporterParameters param, EntityManager _em) throws Exception {
        FileOutputStream fos = null;
        ZipOutputStream zipStream = null;
        try {
            this.param = param;
            if (_em == null) {
                emf = Persistence.createEntityManagerFactory("edas2");
                em = emf.createEntityManager();
            } else {
                em = _em;
            }
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            fos = new FileOutputStream(path + fileName + exportType);
            if (exportType.equals(EXPORT_TYPE_ZIP)) {
                zipStream = new ZipOutputStream(fos);
                xml = factory.createXMLStreamWriter(zipStream);
                zipStream.setMethod(ZipOutputStream.DEFLATED);
                zipStream.setLevel(ZIP_COMPRESSION_LEVEL);
                ZipEntry zipEntry = new ZipEntry(path + fileName + ".xml");
                zipStream.putNextEntry(zipEntry);
            } else {
                xml = factory.createXMLStreamWriter(fos);
            }
            createDocument();
            xml.flush();
            if (exportType.equals(EXPORT_TYPE_ZIP)) zipStream.flush();
        } finally {
            if (exportType.equals(EXPORT_TYPE_ZIP)) try {
                zipStream.closeEntry();
            } catch (Exception e) {
            }
            try {
                xml.close();
            } catch (Exception e) {
            }
            if (exportType.equals(EXPORT_TYPE_ZIP)) try {
                zipStream.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
            if (_em == null) {
                try {
                    em.clear();
                } catch (Exception e) {
                }
                try {
                    emf.close();
                } catch (Exception e) {
                }
            }
        }
    }
