    public static void copyAttributes(XMLInput in, XMLOutput out) throws XMLStreamException {
        while (in.peek() instanceof AttributeEvent) {
            out.write(in.read());
        }
    }
