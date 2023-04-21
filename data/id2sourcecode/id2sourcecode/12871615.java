    private boolean writeFile(StartElement start, XMLEventReader reader, PrintWriter writer) throws UnsupportedEncodingException, IOException, XMLStreamException, CoreException, TransformException, SaxonApiException {
        Attribute aname = start.getAttributeByName(kATTR_NAME);
        if (aname == null) throw new InvalidArgumentException("Element FILE requries attribute name");
        String name = aname.getValue();
        PrintWriter w = getShell().getEnv().getOutput(getShell().getFile(name), false).asPrintWriter(mSerializeOpts);
        boolean ret = parse(reader, w, false);
        w.close();
        return ret;
    }
