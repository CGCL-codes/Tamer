    private void addOriginSigsRels(String signatureZipEntryName, ZipOutputStream zipOutputStream) throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document originSignRelsDocument = documentBuilder.newDocument();
        Element relationshipsElement = originSignRelsDocument.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationships");
        relationshipsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns", "http://schemas.openxmlformats.org/package/2006/relationships");
        originSignRelsDocument.appendChild(relationshipsElement);
        Element relationshipElement = originSignRelsDocument.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship");
        String relationshipId = "rel-" + UUID.randomUUID().toString();
        relationshipElement.setAttribute("Id", relationshipId);
        relationshipElement.setAttribute("Type", "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/signature");
        String target = FilenameUtils.getName(signatureZipEntryName);
        LOG.debug("target: " + target);
        relationshipElement.setAttribute("Target", target);
        relationshipsElement.appendChild(relationshipElement);
        zipOutputStream.putNextEntry(new ZipEntry("_xmlsignatures/_rels/origin.sigs.rels"));
        writeDocumentNoClosing(originSignRelsDocument, zipOutputStream, false);
    }
