    private void readStructureChannel() throws IOException {
        m_channelKeeper.reset();
        boolean reached = false;
        EventCode eventCodeItem;
        while (!reached && (eventCodeItem = m_documentGrammarState.getNextEventCodes()) != null) {
            final EventType eventType;
            eventType = readEventType(eventCodeItem);
            String name;
            String prefix, publicId, systemId;
            CharacterSequence text;
            ScannerValueHolder textProvider;
            ScannerChannel channel;
            final XMLLocusItem locusItem;
            int tp;
            byte itemType;
            switch(itemType = eventType.itemType) {
                case EventCode.ITEM_SD:
                    m_documentGrammarState.startDocument();
                    m_eventList.add(eventType.asEXIEvent());
                    break;
                case EventCode.ITEM_DTD:
                    name = readText().makeString();
                    text = readText();
                    publicId = text.length() != 0 ? text.makeString() : null;
                    text = readText();
                    systemId = text.length() != 0 ? text.makeString() : null;
                    text = readText();
                    m_eventList.add(new EXIEventDTD(name, publicId, systemId, text, eventType));
                    break;
                case EventCode.ITEM_SCHEMA_SE:
                case EventCode.ITEM_SE:
                    readQName(qname, eventType);
                    pushLocusItem(qname.namespaceName, qname.localName);
                    m_documentGrammarState.startElement(eventType.getIndex(), qname.namespaceName, qname.localName);
                    if (m_preserveNS) m_eventList.add(new EXIEventElement(qname.prefix, eventType)); else m_eventList.add((EXIEvent) eventType);
                    break;
                case EventCode.ITEM_SCHEMA_AT:
                case EventCode.ITEM_AT:
                    readQName(qname, eventType);
                    prefix = qname.prefix;
                    if (itemType == EventCode.ITEM_SCHEMA_AT) m_documentGrammarState.attribute(eventType.getIndex(), qname.namespaceName, qname.localName);
                    if (itemType == EventCode.ITEM_AT && "type".equals(qname.localName) && XmlUriConst.W3C_2001_XMLSCHEMA_INSTANCE_URI.equals(qname.namespaceName)) {
                        m_eventList.add(readXsiTypeValue(prefix, eventType));
                    } else {
                        tp = EXISchema.NIL_NODE;
                        EventTypeSchemaAttribute eventTypeSchemaAttribute;
                        if (itemType == EventCode.ITEM_SCHEMA_AT && (eventTypeSchemaAttribute = (EventTypeSchemaAttribute) eventType).useSpecificType()) {
                            int attr = eventTypeSchemaAttribute.getSchemaSubstance();
                            assert EXISchema.ATTRIBUTE_NODE == m_schema.getNodeType(attr);
                            tp = m_schema.getTypeOfAttr(attr);
                        }
                        textProvider = new ScannerValueHolder(qname.localName, qname.namespaceName, tp);
                        channel = (ScannerChannel) m_channelKeeper.getChannel(qname.localName, qname.namespaceName);
                        reached = m_channelKeeper.incrementValueCount(channel);
                        channel.values.add(textProvider);
                        m_eventList.add(new EXIEventAttributeByRef(textProvider, prefix, eventType));
                    }
                    break;
                case EventCode.ITEM_SCHEMA_UNDECLARED_AT_INVALID_VALUE:
                    readQName(qname, eventType);
                    textProvider = new ScannerValueHolder(qname.localName, qname.namespaceName, EXISchema.NIL_NODE);
                    channel = (ScannerChannel) m_channelKeeper.getChannel(qname.localName, qname.namespaceName);
                    reached = m_channelKeeper.incrementValueCount(channel);
                    channel.values.add(textProvider);
                    m_documentGrammarState.attribute(((EventTypeSchema) eventType).serial, qname.namespaceName, qname.localName);
                    m_eventList.add(new EXIEventAttributeByRef(textProvider, (EventType) eventType));
                    break;
                case EventCode.ITEM_SCHEMA_CH:
                    m_documentGrammarState.characters();
                    tp = ((EventTypeSchema) eventType).getSchemaSubstance();
                    locusItem = m_locusStack[m_locusLastDepth];
                    textProvider = new ScannerValueHolder(locusItem.elementLocalName, locusItem.elementURI, tp);
                    channel = (ScannerChannel) m_channelKeeper.getChannel(locusItem.elementLocalName, locusItem.elementURI);
                    reached = m_channelKeeper.incrementValueCount(channel);
                    channel.values.add(textProvider);
                    m_eventList.add(new EXIEventSchemaCharactersByRef(textProvider, eventType));
                    break;
                case EventCode.ITEM_SCHEMA_CH_MIXED:
                    m_documentGrammarState.undeclaredCharacters();
                    locusItem = m_locusStack[m_locusLastDepth];
                    textProvider = new ScannerValueHolder(locusItem.elementLocalName, locusItem.elementURI, EXISchema.NIL_NODE);
                    channel = (ScannerChannel) m_channelKeeper.getChannel(locusItem.elementLocalName, locusItem.elementURI);
                    reached = m_channelKeeper.incrementValueCount(channel);
                    channel.values.add(textProvider);
                    m_eventList.add(new EXIEventSchemaMixedCharactersByRef(textProvider, eventType));
                    break;
                case EventCode.ITEM_CH:
                    m_documentGrammarState.undeclaredCharacters();
                    locusItem = m_locusStack[m_locusLastDepth];
                    textProvider = new ScannerValueHolder(locusItem.elementLocalName, locusItem.elementURI, EXISchema.NIL_NODE);
                    channel = (ScannerChannel) m_channelKeeper.getChannel(locusItem.elementLocalName, locusItem.elementURI);
                    reached = m_channelKeeper.incrementValueCount(channel);
                    channel.values.add(textProvider);
                    m_eventList.add(new EXIEventUndeclaredCharactersByRef(textProvider, eventType));
                    break;
                case EventCode.ITEM_SCHEMA_EE:
                case EventCode.ITEM_EE:
                    m_documentGrammarState.endElement("", "");
                    --m_locusLastDepth;
                    m_eventList.add((EXIEvent) eventType);
                    break;
                case EventCode.ITEM_ED:
                    m_documentGrammarState.endDocument();
                    m_eventList.add(eventType.asEXIEvent());
                    m_foundED = true;
                    break;
                case EventCode.ITEM_SCHEMA_WC_ANY:
                case EventCode.ITEM_SCHEMA_WC_NS:
                    readQName(qname, eventType);
                    pushLocusItem(qname.namespaceName, qname.localName);
                    m_documentGrammarState.startElement(eventType.getIndex(), qname.namespaceName, qname.localName);
                    m_eventList.add(new EXIEventWildcardStartElement(qname.namespaceName, qname.localName, qname.prefix, eventType));
                    break;
                case EventCode.ITEM_SE_WC:
                    readQName(qname, eventType);
                    pushLocusItem(qname.namespaceName, qname.localName);
                    m_documentGrammarState.startUndeclaredElement(qname.namespaceName, qname.localName);
                    m_eventList.add(new EXIEventUndeclaredElement(qname.namespaceName, qname.localName, qname.prefix, eventType));
                    break;
                case EventCode.ITEM_SCHEMA_AT_WC_ANY:
                case EventCode.ITEM_SCHEMA_AT_WC_NS:
                case EventCode.ITEM_AT_WC_ANY_UNTYPED:
                    readQName(qname, eventType);
                    prefix = qname.prefix;
                    m_documentGrammarState.undeclaredAttribute(qname.namespaceName, qname.localName);
                    if ("type".equals(qname.localName) && XmlUriConst.W3C_2001_XMLSCHEMA_INSTANCE_URI.equals(qname.namespaceName)) {
                        assert itemType == EventCode.ITEM_AT_WC_ANY_UNTYPED;
                        m_eventList.add(readXsiTypeValue(prefix, eventType));
                    } else {
                        tp = EXISchema.NIL_NODE;
                        if (itemType != EventCode.ITEM_AT_WC_ANY_UNTYPED) {
                            int ns;
                            if ((ns = m_schema.getNamespaceOfSchema(qname.namespaceName)) != EXISchema.NIL_NODE) {
                                int attr;
                                if ((attr = m_schema.getAttrOfNamespace(ns, qname.localName)) != EXISchema.NIL_NODE) {
                                    tp = m_schema.getTypeOfAttr(attr);
                                }
                            }
                        }
                        textProvider = new ScannerValueHolder(qname.localName, qname.namespaceName, tp);
                        channel = (ScannerChannel) m_channelKeeper.getChannel(qname.localName, qname.namespaceName);
                        reached = m_channelKeeper.incrementValueCount(channel);
                        channel.values.add(textProvider);
                        m_eventList.add(new EXIEventWildcardAttributeByRef(qname.namespaceName, qname.localName, prefix, textProvider, eventType));
                    }
                    break;
                case EventCode.ITEM_SCHEMA_NIL:
                    readQName(qname, eventType);
                    final EXIEventSchemaNil eventSchemaNil = readXsiNilValue(qname.prefix, eventType);
                    if (eventSchemaNil.isNilled()) {
                        m_documentGrammarState.nillify();
                    }
                    m_eventList.add(eventSchemaNil);
                    break;
                case EventCode.ITEM_SCHEMA_TYPE:
                    readQName(qname, eventType);
                    prefix = qname.prefix;
                    m_eventList.add(readXsiTypeValue(prefix, eventType));
                    break;
                case EventCode.ITEM_NS:
                    m_eventList.add(readNS(eventType));
                    break;
                case EventCode.ITEM_SC:
                    throw new UnsupportedOperationException("Event type SC is not supported yet.");
                case EventCode.ITEM_PI:
                    m_documentGrammarState.miscContent();
                    name = readText().makeString();
                    text = readText();
                    m_eventList.add(new EXIEventProcessingInstruction(name, text, eventType));
                    break;
                case EventCode.ITEM_CM:
                    m_documentGrammarState.miscContent();
                    text = readText();
                    m_eventList.add(new EXIEventComment(text, eventType));
                    break;
                case EventCode.ITEM_ER:
                    m_documentGrammarState.miscContent();
                    name = readText().makeString();
                    m_eventList.add(new EXIEventEntityReference(name, eventType));
                    break;
                default:
                    assert false;
                    break;
            }
        }
        if (reached) {
            readMore: do {
                EventTypeList eventTypeList = m_documentGrammarState.getNextEventTypes();
                final int n_eventTypes;
                if ((n_eventTypes = eventTypeList.getLength()) != 1) {
                    assert n_eventTypes > 1;
                    break;
                }
                EventType eventType;
                switch((eventType = eventTypeList.item(0)).itemType) {
                    case EventCode.ITEM_SCHEMA_SE:
                        readQName(qname, eventType);
                        pushLocusItem(qname.namespaceName, qname.localName);
                        m_documentGrammarState.startElement(eventType.getIndex(), qname.namespaceName, qname.localName);
                        if (m_preserveNS) m_eventList.add(new EXIEventElement(qname.prefix, eventType)); else m_eventList.add((EXIEvent) eventType);
                        break;
                    case EventCode.ITEM_SCHEMA_EE:
                    case EventCode.ITEM_EE:
                        m_documentGrammarState.endElement("", "");
                        --m_locusLastDepth;
                        m_eventList.add((EXIEvent) eventType);
                        break;
                    case EventCode.ITEM_ED:
                        m_documentGrammarState.endDocument();
                        m_eventList.add(eventType.asEXIEvent());
                        m_foundED = true;
                    default:
                        break readMore;
                }
            } while (true);
        }
    }
