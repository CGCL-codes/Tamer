    private void process(String entryName) throws IOException, XMLStreamException {
        final int cllimit = 500;
        final QName att_clcontinue = new QName("clcontinue");
        final QName att_title = new QName("title");
        String clcontinue = null;
        while (true) {
            String url = this.base_api + "?action=query" + "&prop=categories" + "&format=xml" + (clcontinue != null ? "&clcontinue=" + escape(clcontinue) : "") + "&titles=" + escape(entryName) + "&cllimit=" + cllimit;
            clcontinue = null;
            LOG.info(url);
            XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement e = event.asStartElement();
                    String name = e.getName().getLocalPart();
                    Attribute att = null;
                    if (name.equals("cl") && (att = e.getAttributeByName(att_title)) != null) {
                        System.out.println(entryName + "\t" + att.getValue());
                    } else if (name.equals("categories") && (att = e.getAttributeByName(att_clcontinue)) != null) {
                        clcontinue = att.getValue();
                    }
                }
            }
            reader.close();
            if (clcontinue == null) break;
        }
    }
