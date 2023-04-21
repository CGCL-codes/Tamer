    public static InstanciaBean decodeInstancia(InputStream inputStream) throws IOException {
        InstanciaBean bean = null;
        Map fileAnexos = new HashMap();
        ZipInputStream zipis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zipis.getNextEntry();
        while (zipEntry != null) {
            log.debug("Processant " + zipEntry.getName());
            if (zipEntry.getName().equals(FORM_DATA_FILE)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int n;
                while ((n = zipis.read()) > -1) baos.write(n);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                XMLDecoder decoder = new XMLDecoder(bais);
                bean = (InstanciaBean) decoder.readObject();
                decoder.close();
                zipis.closeEntry();
            } else if (zipEntry.getName().startsWith(ANEXO_SUBDIR)) {
                String name = zipEntry.getName().substring(ANEXO_SUBDIR.length());
                String contentType = zipEntry.getComment();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int n;
                while ((n = zipis.read()) > -1) baos.write(n);
                log.debug("llegits " + baos.size() + " bytes");
                Anexo anexo = new Anexo(name, contentType, baos.toByteArray());
                fileAnexos.put(zipEntry.getName(), anexo);
                zipis.closeEntry();
            } else {
                log.warn("Entrada " + zipEntry.getName() + " descartada");
                zipis.closeEntry();
            }
            zipEntry = zipis.getNextEntry();
        }
        zipis.close();
        if (bean == null) {
            log.error("No s'ha trobat l'entrada " + FORM_DATA_FILE);
            throw new IOException(FORM_DATA_FILE + " no trobat");
        }
        Map anexos = bean.getAnexos();
        for (Iterator iterAnexo = anexos.keySet().iterator(); iterAnexo.hasNext(); ) {
            String key = (String) iterAnexo.next();
            String entryName = (String) anexos.get(key);
            anexos.put(key, fileAnexos.get(entryName));
        }
        return bean;
    }
