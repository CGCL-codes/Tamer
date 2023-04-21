    private String getStringXML(XMLStreamReader reader) throws XMLStreamException {
        StreamingOMSerializer ser = new StreamingOMSerializer();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLStreamWriter writer = StAXUtils.createXMLStreamWriter(byteArrayOutputStream);
        ser.serialize(new StreamWrapper(reader), writer);
        writer.flush();
        return byteArrayOutputStream.toString();
    }
