            public void readImpl(PipelineContext pipelineContext, ContentHandler contentHandler) {
                Writer writer = new ContentHandlerWriter(contentHandler);
                Config config = readConfig(pipelineContext);
                String encoding = getEncoding(config, null, DEFAULT_ENCODING);
                String contentType = getContentType(config, null, getDefaultContentType());
                try {
                    AttributesImpl attributes = new AttributesImpl();
                    contentHandler.startPrefixMapping(XMLConstants.XSI_PREFIX, XMLConstants.XSI_URI);
                    contentHandler.startPrefixMapping(XMLConstants.XSD_PREFIX, XMLConstants.XSD_URI);
                    attributes.addAttribute(XMLConstants.XSI_URI, "type", "xsi:type", "CDATA", XMLConstants.XS_STRING_QNAME.getQualifiedName());
                    if (contentType != null) attributes.addAttribute("", "content-type", "content-type", "CDATA", contentType + "; charset=" + encoding);
                    contentHandler.startDocument();
                    contentHandler.startElement("", DEFAULT_TEXT_DOCUMENT_ELEMENT, DEFAULT_TEXT_DOCUMENT_ELEMENT, attributes);
                    readInput(pipelineContext, getInputByName(INPUT_DATA), config, writer);
                    contentHandler.endElement("", DEFAULT_TEXT_DOCUMENT_ELEMENT, DEFAULT_TEXT_DOCUMENT_ELEMENT);
                    contentHandler.endDocument();
                } catch (SAXException e) {
                    throw new OXFException(e);
                }
            }
