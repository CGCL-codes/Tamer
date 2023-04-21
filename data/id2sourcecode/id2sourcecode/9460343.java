    private void split(String systemId, InputStream is, PrintWriter listWriter) throws XMLStreamException, IOException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.valueOf(true));
        XMLEventReader xmlreader = inputFactory.createXMLEventReader(systemId, is);
        List<Namespace> ns = null;
        while (xmlreader.hasNext()) {
            XMLEvent e = xmlreader.nextEvent();
            if (e.getEventType() != XMLStreamConstants.START_ELEMENT) {
                if (mNoDTD && e.getEventType() == XMLStreamConstants.DTD) continue;
                if (mNoPI && e.getEventType() == XMLStreamConstants.PROCESSING_INSTRUCTION) continue;
                mHeader.add(e);
                continue;
            }
            if (mNoRoot) {
                StartElement se = e.asStartElement();
                Iterator<?> nsi = se.getNamespaces();
                ns = new ArrayList<Namespace>();
                while (nsi.hasNext()) ns.add((Namespace) nsi.next());
            }
            if (!mNoRoot) {
                if (mRootNode == null) mHeader.add(e); else mHeader.add(mRootNode);
            }
            break;
        }
        while (xmlreader.hasNext()) {
            XMLEvent e = xmlreader.nextEvent();
            int type = e.getEventType();
            if (type == XMLStreamConstants.START_ELEMENT) {
                write(xmlreader, e, ns, listWriter);
            } else if (type == XMLStreamConstants.END_ELEMENT || type == XMLStreamConstants.END_DOCUMENT) {
            } else {
                if (type == XMLStreamConstants.CHARACTERS && e.asCharacters().isWhiteSpace()) continue;
                printErr("Skipping XML node: " + e.toString());
            }
        }
        xmlreader.close();
    }
