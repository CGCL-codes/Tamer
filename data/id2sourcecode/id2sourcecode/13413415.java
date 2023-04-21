    protected boolean serializeXOPInclude(XMLStreamReader reader, XMLStreamWriter writer) {
        String cid = ElementHelper.getContentID(reader);
        DataHandler dh = getDataHandler(cid, (OMAttachmentAccessor) reader);
        if (dh == null) {
            return false;
        }
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMText omText = omFactory.createOMText(dh, true);
        omText.setContentID(cid);
        MTOMXMLStreamWriter mtomWriter = (writer instanceof MTOMXMLStreamWriter) ? (MTOMXMLStreamWriter) writer : null;
        if (mtomWriter != null && mtomWriter.isOptimized() && mtomWriter.isOptimizedThreshold(omText)) {
            mtomWriter.writeOptimized(omText);
            return false;
        }
        omText.setOptimize(false);
        try {
            writer.writeCharacters(omText.getText());
            return true;
        } catch (XMLStreamException e) {
            return false;
        }
    }
