    public ByteArrayOutputStream exportStream(String exportType, WQXExporterParameters param, EntityManager _em) throws Exception {
        ByteArrayOutputStream baos = null;
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
            baos = new ByteArrayOutputStream();
            if (exportType.equals(EXPORT_TYPE_ZIP)) {
                zipStream = new ZipOutputStream(baos);
                xml = factory.createXMLStreamWriter(zipStream);
                zipStream.setMethod(ZipOutputStream.DEFLATED);
                zipStream.setLevel(ZIP_COMPRESSION_LEVEL);
                ZipEntry zipEntry = new ZipEntry("edas2wqx.xml");
                zipStream.putNextEntry(zipEntry);
            } else {
                xml = factory.createXMLStreamWriter(baos);
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
                baos.close();
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
        return baos;
    }
