    private String getSignatureResourceName(URL url) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        InputStream inputStream = url.openStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (false == "[Content_Types].xml".equals(zipEntry.getName())) {
                continue;
            }
            Document contentTypesDocument = loadDocument(zipInputStream);
            Element nsElement = contentTypesDocument.createElement("ns");
            nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:tns", "http://schemas.openxmlformats.org/package/2006/content-types");
            NodeList nodeList = XPathAPI.selectNodeList(contentTypesDocument, "/tns:Types/tns:Override[@ContentType='application/vnd.openxmlformats-package.digital-signature-xmlsignature+xml']/@PartName", nsElement);
            if (nodeList.getLength() == 0) {
                return null;
            }
            String partName = nodeList.item(0).getTextContent();
            LOG.debug("part name: " + partName);
            partName = partName.substring(1);
            return partName;
        }
        return null;
    }
