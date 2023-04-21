    public void export(XMLExportable e, String tagName) {
        XMLTag tag = new XMLTag(tagName);
        e.exportData(tag, this);
        Element elem = XMLTreeUtil.convertToXMLDocElement(doc, tag);
        doc.appendChild(elem);
        try {
            zipOut.putNextEntry(new ZipEntry("Main.xml"));
            FileHandler.writeDocument(doc, zipOut);
            zipOut.close();
        } catch (Exception ex) {
            System.out.println("Error writing main document");
            ex.printStackTrace();
        }
    }
