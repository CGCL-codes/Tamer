    protected void Process() throws IOException, XMLStreamException {
        try {
            int eventType = reader.getEventType();
            while (reader.hasNext()) {
                switch(eventType) {
                    case XMLStreamConstants.START_DOCUMENT:
                        if (!isPartial && writer != null) {
                            writer.writeStartDocument(reader.getEncoding(), reader.getVersion());
                            writer.setNamespaceContext(reader.getNamespaceContext());
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        if (!isPartial && writer != null) writer.writeEndDocument();
                        break;
                    case XMLStreamConstants.CDATA:
                        if (writer != null) writer.writeCData(reader.getText());
                        break;
                    case XMLStreamConstants.COMMENT:
                        if (writer != null) writer.writeComment(reader.getText());
                        break;
                    case XMLStreamConstants.DTD:
                        if (writer != null) writer.writeDTD(reader.getText());
                        break;
                    case XMLStreamConstants.ENTITY_REFERENCE:
                        if (writer != null) writer.writeEntityRef(reader.getLocalName());
                        break;
                    case XMLStreamConstants.NAMESPACE:
                        _processNamespaces();
                        break;
                    case XMLStreamConstants.PROCESSING_INSTRUCTION:
                        if (writer != null) {
                            String pIData = reader.getPIData();
                            if (pIData == null) writer.writeProcessingInstruction(reader.getPITarget()); else writer.writeProcessingInstruction(reader.getPITarget(), pIData);
                        }
                        break;
                    case XMLStreamConstants.SPACE:
                        if (writer != null) writer.writeCharacters(reader.getText());
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        if (_processStartElement()) {
                            _processNamespaces();
                            _processAttributes();
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (!handleEndElement(reader.getLocalName()) && writer != null) writer.writeEndElement();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (!handleContent(reader.getText()) && writer != null) writer.writeCharacters(reader.getText());
                        break;
                    case XMLStreamConstants.ATTRIBUTE:
                        _processAttributes();
                        break;
                }
                if (stop) return;
                eventType = reader.next();
            }
            if (!isPartial && writer != null) writer.close();
            reader.close();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
    }
