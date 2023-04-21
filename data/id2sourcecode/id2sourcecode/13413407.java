    protected void serializeElement(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        ArrayList writePrefixList = null;
        ArrayList writeNSList = null;
        String ePrefix = reader.getPrefix();
        ePrefix = (ePrefix != null && ePrefix.length() == 0) ? null : ePrefix;
        String eNamespace = reader.getNamespaceURI();
        eNamespace = (eNamespace != null && eNamespace.length() == 0) ? null : eNamespace;
        if (this.inputHasAttachments && XOP_INCLUDE.getNamespaceURI().equals(eNamespace)) {
            String eLocalPart = reader.getLocalName();
            if (XOP_INCLUDE.getLocalPart().equals(eLocalPart)) {
                if (serializeXOPInclude(reader, writer)) {
                    skipEndElement = true;
                    return;
                }
            }
        }
        boolean setPrefixFirst = OMSerializerUtil.isSetPrefixBeforeStartElement(writer);
        if (!setPrefixFirst) {
            if (eNamespace != null) {
                if (ePrefix == null) {
                    if (!OMSerializerUtil.isAssociated("", eNamespace, writer)) {
                        if (writePrefixList == null) {
                            writePrefixList = new ArrayList();
                            writeNSList = new ArrayList();
                        }
                        writePrefixList.add("");
                        writeNSList.add(eNamespace);
                    }
                    writer.writeStartElement("", reader.getLocalName(), eNamespace);
                } else {
                    if (!OMSerializerUtil.isAssociated(ePrefix, eNamespace, writer)) {
                        if (writePrefixList == null) {
                            writePrefixList = new ArrayList();
                            writeNSList = new ArrayList();
                        }
                        writePrefixList.add(ePrefix);
                        writeNSList.add(eNamespace);
                    }
                    writer.writeStartElement(ePrefix, reader.getLocalName(), eNamespace);
                }
            } else {
                writer.writeStartElement(reader.getLocalName());
            }
        }
        int count = reader.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            String prefix = reader.getNamespacePrefix(i);
            prefix = (prefix != null && prefix.length() == 0) ? null : prefix;
            String namespace = reader.getNamespaceURI(i);
            namespace = (namespace != null && namespace.length() == 0) ? null : namespace;
            String newPrefix = OMSerializerUtil.generateSetPrefix(prefix, namespace, writer, false, setPrefixFirst);
            if (newPrefix != null) {
                if (writePrefixList == null) {
                    writePrefixList = new ArrayList();
                    writeNSList = new ArrayList();
                }
                if (!writePrefixList.contains(newPrefix)) {
                    writePrefixList.add(newPrefix);
                    writeNSList.add(namespace);
                }
            }
        }
        String newPrefix = OMSerializerUtil.generateSetPrefix(ePrefix, eNamespace, writer, false, setPrefixFirst);
        if (newPrefix != null) {
            if (writePrefixList == null) {
                writePrefixList = new ArrayList();
                writeNSList = new ArrayList();
            }
            if (!writePrefixList.contains(newPrefix)) {
                writePrefixList.add(newPrefix);
                writeNSList.add(eNamespace);
            }
        }
        count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String prefix = reader.getAttributePrefix(i);
            prefix = (prefix != null && prefix.length() == 0) ? null : prefix;
            String namespace = reader.getAttributeNamespace(i);
            namespace = (namespace != null && namespace.length() == 0) ? null : namespace;
            if (prefix == null && namespace != null) {
                String writerPrefix = writer.getPrefix(namespace);
                writerPrefix = (writerPrefix != null && writerPrefix.length() == 0) ? null : writerPrefix;
                prefix = (writerPrefix != null) ? writerPrefix : generateUniquePrefix(writer.getNamespaceContext());
            }
            newPrefix = OMSerializerUtil.generateSetPrefix(prefix, namespace, writer, true, setPrefixFirst);
            if (newPrefix != null) {
                if (writePrefixList == null) {
                    writePrefixList = new ArrayList();
                    writeNSList = new ArrayList();
                }
                if (!writePrefixList.contains(newPrefix)) {
                    writePrefixList.add(newPrefix);
                    writeNSList.add(namespace);
                }
            }
        }
        if (setPrefixFirst) {
            if (eNamespace != null) {
                if (ePrefix == null) {
                    writer.writeStartElement("", reader.getLocalName(), eNamespace);
                } else {
                    writer.writeStartElement(ePrefix, reader.getLocalName(), eNamespace);
                }
            } else {
                writer.writeStartElement(reader.getLocalName());
            }
        }
        if (writePrefixList != null) {
            for (int i = 0; i < writePrefixList.size(); i++) {
                String prefix = (String) writePrefixList.get(i);
                String namespace = (String) writeNSList.get(i);
                if (prefix != null) {
                    if (namespace == null) {
                        writer.writeNamespace(prefix, "");
                    } else {
                        writer.writeNamespace(prefix, namespace);
                    }
                } else {
                    writer.writeDefaultNamespace(namespace);
                }
            }
        }
        count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String prefix = reader.getAttributePrefix(i);
            prefix = (prefix != null && prefix.length() == 0) ? null : prefix;
            String namespace = reader.getAttributeNamespace(i);
            namespace = (namespace != null && namespace.length() == 0) ? null : namespace;
            if (prefix == null && namespace != null) {
                prefix = writer.getPrefix(namespace);
                if (prefix == null || "".equals(prefix)) {
                    for (int j = 0; j < writePrefixList.size(); j++) {
                        if (namespace.equals((String) writeNSList.get(j))) {
                            prefix = (String) writePrefixList.get(j);
                        }
                    }
                }
            } else if (namespace != null) {
                String writerPrefix = writer.getPrefix(namespace);
                if (!prefix.equals(writerPrefix) && !"".equals(writerPrefix)) {
                    prefix = writerPrefix;
                }
            }
            if (namespace != null) {
                writer.writeAttribute(prefix, namespace, reader.getAttributeLocalName(i), reader.getAttributeValue(i));
            } else {
                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
            }
        }
    }
