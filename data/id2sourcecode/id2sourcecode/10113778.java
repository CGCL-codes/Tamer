    public int process() throws TransformerException, IOException, SAXException {
        ZipInputStream zis = new ZipInputStream(input);
        final ZipOutputStream zos = new ZipOutputStream(output);
        final OutputStreamWriter osw = new OutputStreamWriter(zos);
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        TransformerFactory tf = TransformerFactory.newInstance();
        if (!tf.getFeature(SAXSource.FEATURE) || !tf.getFeature(SAXResult.FEATURE)) {
            return 0;
        }
        SAXTransformerFactory saxtf = (SAXTransformerFactory) tf;
        Templates templates = null;
        if (xslt != null) {
            templates = saxtf.newTemplates(xslt);
        }
        EntryElement entryElement = getEntryElement(zos);
        ContentHandler outDocHandler = null;
        switch(outRepresentation) {
            case BYTECODE:
                outDocHandler = new OutputSlicingHandler(new ASMContentHandlerFactory(zos, computeMax), entryElement, false);
                break;
            case MULTI_XML:
                outDocHandler = new OutputSlicingHandler(new SAXWriterFactory(osw, true), entryElement, true);
                break;
            case SINGLE_XML:
                ZipEntry outputEntry = new ZipEntry(SINGLE_XML_NAME);
                zos.putNextEntry(outputEntry);
                outDocHandler = new SAXWriter(osw, false);
                break;
        }
        ContentHandler inDocHandler = null;
        if (templates == null) {
            inDocHandler = outDocHandler;
        } else {
            inDocHandler = new InputSlicingHandler("class", outDocHandler, new TransformerHandlerFactory(saxtf, templates, outDocHandler));
        }
        ContentHandlerFactory inDocHandlerFactory = new SubdocumentHandlerFactory(inDocHandler);
        if (inDocHandler != null && inRepresentation != SINGLE_XML) {
            inDocHandler.startDocument();
            inDocHandler.startElement("", "classes", "classes", new AttributesImpl());
        }
        int i = 0;
        ZipEntry ze = null;
        while ((ze = zis.getNextEntry()) != null) {
            update(ze.getName(), n++);
            if (isClassEntry(ze)) {
                processEntry(zis, ze, inDocHandlerFactory);
            } else {
                OutputStream os = entryElement.openEntry(getName(ze));
                copyEntry(zis, os);
                entryElement.closeEntry();
            }
            i++;
        }
        if (inDocHandler != null && inRepresentation != SINGLE_XML) {
            inDocHandler.endElement("", "classes", "classes");
            inDocHandler.endDocument();
        }
        if (outRepresentation == SINGLE_XML) {
            zos.closeEntry();
        }
        zos.flush();
        zos.close();
        return i;
    }
