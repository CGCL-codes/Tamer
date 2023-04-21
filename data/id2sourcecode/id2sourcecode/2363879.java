    private CTRelationships getRelationships(URL url, String relationshipsEntryName) throws IOException, JAXBException {
        ZipInputStream zipInputStream = new ZipInputStream(url.openStream());
        ZipEntry zipEntry;
        InputStream relationshipsInputStream = null;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (false == relationshipsEntryName.equals(zipEntry.getName())) {
                continue;
            }
            relationshipsInputStream = zipInputStream;
            break;
        }
        if (null == relationshipsInputStream) {
            return null;
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(be.fedict.eid.applet.service.signer.jaxb.opc.relationships.ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<CTRelationships> relationshipsElement = (JAXBElement<CTRelationships>) unmarshaller.unmarshal(relationshipsInputStream);
        return relationshipsElement.getValue();
    }
