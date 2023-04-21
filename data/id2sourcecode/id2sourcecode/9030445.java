    private void process(String userName) throws IOException, XMLStreamException {
        final int uclimit = 500;
        final QName Attucstart = new QName("ucstart");
        String ucstart = null;
        String ucnamespace = null;
        if (!this.ucnamespaces.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer i : this.ucnamespaces) {
                if (sb.length() > 0) sb.append("|");
                sb.append(String.valueOf(i));
            }
            ucnamespace = sb.toString();
        }
        while (true) {
            String url = this.base_api + "?action=query" + "&list=usercontribs" + "&format=xml" + (ucnamespace == null ? "" : "&ucnamespace=" + ucnamespace) + (ucstart != null ? "&ucstart=" + escape(ucstart) : "") + (this.use_prefix ? "&ucuserprefix=" : "&ucuser=") + escape(userName) + "&uclimit=" + uclimit;
            ucstart = null;
            LOG.info(url);
            XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement e = event.asStartElement();
                    String name = e.getName().getLocalPart();
                    if (name.equals("item")) {
                        System.out.println(userName + "\t" + attr(e, "revid") + "\t" + attr(e, "pageid") + "\t" + attr(e, "ns") + "\t" + attr(e, "title") + "\t" + attr(e, "timestamp") + "\t" + attr(e, "comment") + "\t" + attr(e, "is_new") + "\t" + attr(e, "is_top") + "\t" + attr(e, "is_minor") + "\t");
                    } else if (name.equals("usercontribs")) {
                        Attribute clcont = e.getAttributeByName(Attucstart);
                        if (clcont != null) {
                            ucstart = clcont.getValue();
                        }
                    }
                }
            }
            reader.close();
            if (ucstart == null) break;
        }
    }
