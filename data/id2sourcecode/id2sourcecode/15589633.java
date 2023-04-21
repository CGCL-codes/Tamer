    public void fix(final DAISYDocumentSet documentSet, final EventListener listener, final ViolationCollatingErrorHandler errorHandler) throws IOException, JDOMException {
        final DocType docType = new DocType("package");
        opfDocument.setDocType(docType);
        docType.setPublicID("+//ISBN 0-9673008-1-9//DTD OEB 1.2 Package//EN");
        docType.setSystemID("http://openebook.org/dtds/oeb-1.0.1/oebpkg101.dtd");
        fixFormatOfElements();
        fixPackage();
        fixMetadata();
        fixManifest(documentSet.getFile(DocumentComponent.XML).getName(), documentSet.getFile(DocumentComponent.NCX).getName(), documentSet.getFile(DocumentComponent.SMIL).getName(), opfFile.getName());
        fixSpine();
        fixGuide();
        if (!opfFile.getParentFile().equals(documentSet.getFileBasePath())) {
            opfFile = FileUtils.copyFileToDirectory(opfFile, documentSet.getFileBasePath());
            documentSet.overwriteFiles(DocumentComponent.OPF, opfFile);
        }
        FileUtils.saveDocumentToFile(opfDocument, opfFile, true);
        elements = XMLParseUtils.getDescendentsAsMap(opfDocument.getRootElement());
        listener.message("Modifying OPF File: " + opfFile.getAbsolutePath());
        documentSet.createDocuments(DocumentComponent.OPF, true);
    }
