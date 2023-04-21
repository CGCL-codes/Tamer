    public void content(Element e) throws IOException, XmlException {
        ElementRule rule = dtd.getElementRule(e.getName());
        ElementRuleState eruleState = null;
        boolean pcdata = true;
        if (rule != null) {
            pcdata = rule.isPCDataAllowed();
            if (inEntityScan) eruleState = eruleStack.state(); else eruleState = eruleStack.startElement();
            if (!inEntityScan) eruleState.clear();
        }
        Writer w = NullWriter.getInstance();
        CharacterData cd = null;
        whileLoop: while (true) {
            if (!pcdata) S();
            switch(scanner.peekEvent()) {
                case XmlEvent.ETAG:
                    if (cd != null) e.appendChild(cd);
                    break whileLoop;
                case XmlEvent.STAG:
                    if (cd != null) e.appendChild(cd);
                    Element child = element();
                    if (rule != null) rule.encounterElement(child, eruleState);
                    e.appendChild(child);
                    break;
                case XmlEvent.COMMENT:
                    if (cd != null) e.appendChild(cd);
                    Comment c = comment(false);
                    if (c != null) e.appendChild(c);
                    break;
                case XmlEvent.PI:
                    if (cd != null) e.appendChild(cd);
                    PI pi = pi(false);
                    if (pi != null) e.appendChild(pi);
                    break;
                case XmlEvent.CHARDATA:
                    if (!pcdata) throw new ElementRuleException("Not allowed to have PCDATA section: " + e, rule);
                    if (cd == null) {
                        cd = new CharacterData();
                        w = cd.getWriter();
                    }
                    CharData(w);
                    break;
                case XmlEvent.CDSECT:
                    if (!pcdata) throw new ElementRuleException("Not allowed to have PCDATA section: " + e, rule);
                    if (cd == null) {
                        cd = new CharacterData();
                        w = cd.getWriter();
                    }
                    if (!CDSect(w)) {
                        throw new XmlException("Bad CDSect tag found");
                    }
                    break;
                case XmlEvent.REFERENCE:
                    Entity entity = Reference();
                    if (entity != null) {
                        entityScan(e, entity);
                    } else {
                        if (!pcdata) throw new ElementRuleException("Not allowed to have PCDATA section: " + e, rule);
                        if (cd == null) {
                            cd = new CharacterData();
                            w = cd.getWriter();
                        }
                        w.write((char) scanner.read());
                    }
                    break;
                case XmlEvent.NONE:
                    throw new XmlException("Illegal content for element");
                case XmlEvent.EOD:
                    if (inEntityScan) {
                        if (cd != null) e.appendChild(cd);
                        break whileLoop;
                    }
                    throw new XmlException("EOF in scanning");
                default:
                    throw new XmlException("Unknown content for element " + e);
            }
        }
        if (rule != null) {
            if (!inEntityScan) {
                rule.encounterEnd(eruleState);
                eruleStack.endElement();
            }
        }
    }
