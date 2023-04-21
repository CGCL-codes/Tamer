    private void extractItems(String rssOs, XMLStreamWriter writer) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        String items = "";
        try {
            reader = factory.createXMLStreamReader(new StringReader(rssOs));
            boolean finish = false;
            boolean inItems = false;
            boolean inTotalResults = false;
            while (reader.hasNext() && !finish) {
                int type = reader.next();
                switch(type) {
                    case XMLStreamReader.START_ELEMENT:
                        if (inItems) {
                            writer.writeStartElement(reader.getLocalName());
                        }
                        if (reader.getLocalName().equals("item")) {
                            inItems = true;
                            items += "<item>";
                            writer.writeStartElement(reader.getLocalName());
                        }
                        if (reader.getLocalName().equals("totalResults")) {
                            inTotalResults = true;
                        }
                        break;
                    case XMLStreamReader.CHARACTERS:
                        if (inItems) {
                            writer.writeCharacters(reader.getText());
                        }
                        if (inTotalResults) {
                            totalResults += Integer.valueOf(reader.getText());
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        if (reader.getLocalName().equals("item")) {
                            inItems = false;
                            writer.writeEndElement();
                        }
                        if (reader.getLocalName().equals("totalResults")) {
                            inTotalResults = false;
                        }
                        if (inItems) {
                            writer.writeEndElement();
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XMLStreamException e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
            }
        }
    }
