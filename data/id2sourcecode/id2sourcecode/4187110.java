    private static void copy(File source, File destination) throws IOException, DocumentException {
        SWFReader reader = new SWFReader(new FileInputStream(source));
        SWFDocumentReader docReader = new SWFDocumentReader();
        reader.addListener(docReader);
        reader.read();
        SWFDocument sourceDoc = docReader.getDocument();
        XMLWriter xmlWriter = new XMLWriter(sourceDoc);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        xmlWriter.write(baos, false);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        XMLReader xmlReader = new XMLReader(bais);
        SWFDocument targetDoc = xmlReader.getDocument();
        SWFWriter writer = new SWFWriter(targetDoc, new FileOutputStream(destination));
        writer.write();
    }
