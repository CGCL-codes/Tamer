    private void split(InputStream is) throws XMLStreamException, IOException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.valueOf(true));
        XMLEventReader xmlreader = inputFactory.createXMLEventReader(is);
        while (xmlreader.hasNext()) {
            XMLEvent e = xmlreader.nextEvent();
            if (e.getEventType() != XMLStreamConstants.START_ELEMENT) {
                mHeader.add(e);
                continue;
            }
            if (mRootNode == null) mHeader.add(e); else mHeader.add(mRootNode);
            break;
        }
        while (xmlreader.hasNext()) {
            XMLEvent e = xmlreader.nextEvent();
            int type = e.getEventType();
            if (type == XMLStreamConstants.START_ELEMENT) {
                write(xmlreader, e);
            } else if (type == XMLStreamConstants.END_ELEMENT || type == XMLStreamConstants.END_DOCUMENT) {
            } else {
                printErr("Skipping XML node: " + e.toString());
            }
        }
        xmlreader.close();
    }
