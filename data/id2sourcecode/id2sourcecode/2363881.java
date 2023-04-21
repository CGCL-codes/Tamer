    private CTTypes getContentTypes(URL url) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        ZipInputStream zipInputStream = new ZipInputStream(url.openStream());
        ZipEntry zipEntry;
        InputStream contentTypesInputStream = null;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (!"[Content_Types].xml".equals(zipEntry.getName())) {
                continue;
            }
            contentTypesInputStream = zipInputStream;
            break;
        }
        if (null == contentTypesInputStream) {
            return null;
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(be.fedict.eid.applet.service.signer.jaxb.opc.contenttypes.ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<CTTypes> contentTypesElement = (JAXBElement<CTTypes>) unmarshaller.unmarshal(contentTypesInputStream);
        return contentTypesElement.getValue();
    }
