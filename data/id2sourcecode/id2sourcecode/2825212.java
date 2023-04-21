    private void process(String page) throws IOException, XMLStreamException {
        final int lllimit = 500;
        final QName att_lang = new QName("lang");
        final QName att_llcontinue = new QName("llcontinue");
        String llcontinue = null;
        String found = null;
        while (true) {
            String url = this.base_api + "?action=query" + "&prop=langlinks" + "&format=xml" + "&redirects" + (llcontinue != null ? "&llcontinue=" + escape(llcontinue) : "") + "&titles=" + escape(page) + "&lllimit=" + lllimit;
            llcontinue = null;
            LOG.info(url);
            XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement e = event.asStartElement();
                    String name = e.getName().getLocalPart();
                    Attribute langAtt = null;
                    if (name.equals("ll") && (langAtt = e.getAttributeByName(att_lang)) != null && langAtt.getValue().equalsIgnoreCase(this.lang)) {
                        found = reader.getElementText();
                        llcontinue = null;
                        break;
                    } else if (name.equals("langlinks")) {
                        Attribute llcont = e.getAttributeByName(att_llcontinue);
                        if (llcont != null) {
                            llcontinue = llcont.getValue();
                        }
                    }
                }
            }
            reader.close();
            if (llcontinue == null) break;
        }
        if (found != null && !inverse_selection) {
            System.out.println(found);
        } else if (found == null && inverse_selection) {
            System.out.println(page);
        }
    }
