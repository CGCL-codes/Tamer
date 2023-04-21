    private void write(XMLEventReader xmlreader, XMLEvent first) throws XMLStreamException, IOException {
        File fout = nextFile();
        OutputStream fo = new FileOutputStream(fout);
        XMLEventWriter w = mOutputFactory.createXMLEventWriter(fo);
        for (XMLEvent e : mHeader) w.add(e);
        w.add(first);
        int depth = 0;
        int nchild = 0;
        while (xmlreader.hasNext()) {
            XMLEvent e = xmlreader.nextEvent();
            w.add(e);
            if (e.getEventType() == XMLStreamConstants.START_ELEMENT) depth++; else if (e.getEventType() == XMLStreamConstants.END_ELEMENT) {
                if (depth-- <= 0) {
                    if (++nchild == mNumChildren) break;
                }
            }
        }
        w.add(mEventFactory.createEndElement(first.asStartElement().getName(), null));
        w.add(mEventFactory.createEndDocument());
        w.close();
        fo.close();
    }
