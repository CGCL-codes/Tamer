    private void add_base(XMLEventReader reader, XMLEventWriter writer, boolean opt_all, boolean opt_relative, String parentURI) throws URISyntaxException, XMLStreamException {
        Stack<String> mURIs = new Stack<String>();
        mURIs.push(parentURI);
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.getEventType() == XMLEvent.START_ELEMENT) {
                boolean addBase = false;
                StartElement start = event.asStartElement();
                String baseURI = getBaseURI(start);
                parentURI = mURIs.lastElement();
                boolean bIsRoot = mURIs.size() == 1;
                if (bIsRoot) {
                    addBase = true;
                } else if (opt_all) addBase = true; else if (baseURI.equals(parentURI)) addBase = false; else if (!Util.isEqual(parentURI, baseURI)) addBase = true;
                if (addBase) {
                    String uri = resolve(parentURI, baseURI, bIsRoot ? false : opt_relative);
                    event = add_base_attr(start, uri);
                } else event = removeBase(start);
                mURIs.push(baseURI);
            } else if (event.getEventType() == XMLEvent.END_ELEMENT) mURIs.pop();
            writer.add(event);
        }
    }
