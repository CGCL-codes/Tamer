    public void save(MessageThread thread, OutputStream out) throws StorageException {
        logger.debug("Calling method save");
        XMLWriter writer = new IndentingXMLWriter(out, "1.0", "UTF-8");
        try {
            writer.startDocument();
            writer.startElement(null, "MessageThread");
            writer.startElement(null, "Id");
            writer.characters(String.valueOf(thread.getId()));
            writer.endElement();
            writer.startElement(null, "Name");
            writer.characters(thread.getName());
            writer.endElement();
            for (Message message : thread.getMessages()) {
                if (message instanceof AzetMessage) {
                    writer.startElement(null, "AzetMessage");
                    saveMessage(writer, message);
                    AzetMessage azetMessage = (AzetMessage) message;
                    if (azetMessage.getSign() != null) {
                        writer.startElement(null, "Sign");
                        writer.characters(azetMessage.getSign());
                        writer.endElement();
                    }
                    if (azetMessage.getTimeOfPrevious() != null) {
                        writer.startElement(null, "TimeOfPrevious");
                        writer.characters(SCHEMA_DATE_FORMAT.format(azetMessage.getTimeOfPrevious()));
                        writer.endElement();
                    }
                    if (azetMessage.getTextOfPrevious() != null) {
                        writer.startElement(null, "TextOfPrevious");
                        writer.characters(azetMessage.getTextOfPrevious());
                        writer.endElement();
                    }
                    if (azetMessage.getLastFragment() != null) {
                        writer.startElement(null, "LastFragment");
                        writer.characters(azetMessage.getLastFragment());
                        writer.endElement();
                    }
                    writer.endElement();
                } else {
                    throw new UnsupportedOperationException("Message type not supported");
                }
            }
            writer.endElement();
            writer.endDocument();
        } catch (XMLWriterException ex) {
            throw new StorageException("Unable to store thread: " + thread.getName(), ex);
        } finally {
            try {
                writer.close();
            } catch (XMLWriterException ex) {
                throw new StorageException("Unable to store thread: " + thread.getName(), ex);
            }
        }
    }
