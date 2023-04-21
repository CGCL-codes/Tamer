    protected void serializeNode(XMLStreamReader reader, XMLStreamWriter writer, boolean startAtNext) throws XMLStreamException {
        boolean useCurrentEvent = !startAtNext;
        while (reader.hasNext() || useCurrentEvent) {
            int event = 0;
            if (useCurrentEvent) {
                event = reader.getEventType();
                useCurrentEvent = false;
            } else {
                event = reader.next();
            }
            if (event == START_ELEMENT) {
                serializeElement(reader, writer);
                depth++;
            } else if (event == ATTRIBUTE) {
                serializeAttributes(reader, writer);
            } else if (event == CHARACTERS) {
                serializeText(reader, writer);
            } else if (event == COMMENT) {
                serializeComment(reader, writer);
            } else if (event == CDATA) {
                serializeCData(reader, writer);
            } else if (event == END_ELEMENT) {
                serializeEndElement(writer);
                depth--;
            } else if (event == START_DOCUMENT) {
                depth++;
            } else if (event == END_DOCUMENT) {
                if (depth != 0) depth--;
                try {
                    serializeEndElement(writer);
                } catch (Exception e) {
                }
            }
            if (depth == 0) {
                break;
            }
        }
    }
