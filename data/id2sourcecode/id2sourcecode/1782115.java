        @Override
        public void write(DahdiLink link, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            LINK_XML.write(link, xml);
            xml.setAttribute(LINK_SPAN, link.getSpan());
            xml.setAttribute(LINK_CHANNEL_ID, link.getChannelID());
            xml.setAttribute(LINK_CODE, link.getCode());
            xml.setAttribute(LINK_IO_BUFFER_SIZE, link.getIoBufferSize());
        }
