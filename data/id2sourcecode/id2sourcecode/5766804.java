    public static void serialize(CAS cas, URL typeSystemDescriptionUrl, OutputStream out) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(out) {

            @Override
            public void close() throws IOException {
            }
        };
        zipOut.setLevel(9);
        ZipEntry typeSystemEntry = new ZipEntry(TYPE_SYSTEM_ENTRY);
        zipOut.putNextEntry(typeSystemEntry);
        try {
            TypeSystemDescription typeSystemDescription = UIMAFramework.getXMLParser().parseTypeSystemDescription(new XMLInputSource(typeSystemDescriptionUrl));
            typeSystemDescription.toXML(zipOut);
        } catch (SAXException e) {
            throw new IOException("Error while serializing type system.", e);
        } catch (InvalidXMLException e) {
            throw new IOException("Error while serializing type system.", e);
        }
        ZipEntry casEntry = new ZipEntry(CAS_ENTRY);
        zipOut.putNextEntry(casEntry);
        try {
            XmiCasSerializer.serialize(cas, zipOut);
        } catch (SAXException e) {
            throw new IOException("Error while serializing CAS.", e);
        }
        zipOut.finish();
        out.close();
    }
