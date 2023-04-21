    private void process(String entry, int level) throws DatabaseException, IOException, XMLStreamException {
        if (level > this.max_depth) return;
        final int limit = 500;
        final QName AttClcontinue = new QName("cmcontinue");
        final QName AttTitle = new QName("title");
        String cmcontinue = null;
        String cmnamespace = "14";
        int count = 0;
        if (!this.cmnamespaces.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer i : this.cmnamespaces) {
                if (sb.length() > 0) sb.append("|");
                sb.append(String.valueOf(i));
            }
            cmnamespace = sb.toString();
        }
        while (true) {
            String url = this.base_api + "?action=query" + "&list=categorymembers" + "&format=xml" + "&cmnamespace=" + cmnamespace + (cmcontinue != null ? "&cmcontinue=" + escape(cmcontinue) : "") + "&cmtitle=" + escape(entry) + "&cmlimit=" + limit;
            cmcontinue = null;
            LOG.info(url);
            XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement e = event.asStartElement();
                    String name = e.getName().getLocalPart();
                    if (name.equals("cm")) {
                        Attribute cat = e.getAttributeByName(AttTitle);
                        if (cat != null) {
                            String rev = cat.getValue();
                            if (!categories.containsKey(txn, rev)) {
                                LOG.info("adding " + rev + " level=" + level);
                                categories.put(txn, rev, level + 1);
                                if (subclass2class != null) {
                                    subclass2class.put(txn, rev, entry);
                                }
                                ++count;
                            }
                        }
                    } else if (name.equals("categorymembers")) {
                        Attribute clcont = e.getAttributeByName(AttClcontinue);
                        if (clcont != null) {
                            cmcontinue = clcont.getValue();
                        }
                    }
                }
            }
            reader.close();
            if (cmcontinue == null) break;
        }
        LOG.info("count(" + entry + ")=" + count);
    }
