    private void processArticle(String entryName) throws IOException, XMLStreamException {
        if (entryName.startsWith("File:") || entryName.startsWith("Image:")) {
            processImage(entryName, entryName);
            return;
        }
        final int cllimit = 500;
        final QName att_imcontinue = new QName("imcontinue");
        final QName att_title = new QName("title");
        String imcontinue = null;
        while (true) {
            Set<String> images = new HashSet<String>();
            String url = this.base_api + "?action=query" + "&prop=images" + "&format=xml" + (imcontinue != null ? "&imcontinue=" + escape(imcontinue) : "") + "&titles=" + escape(entryName) + "&imlimit=" + cllimit;
            imcontinue = null;
            LOG.info(url);
            XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(openStream(url));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement e = event.asStartElement();
                    String name = e.getName().getLocalPart();
                    Attribute att = null;
                    if (name.equals("im") && (att = e.getAttributeByName(att_title)) != null) {
                        images.add(att.getValue());
                    } else if (name.equals("images") && (att = e.getAttributeByName(att_imcontinue)) != null) {
                        imcontinue = att.getValue();
                    }
                }
            }
            reader.close();
            if (images.isEmpty()) {
                LOG.info("No images found for " + entryName);
            }
            for (String s : images) {
                processImage(entryName, s);
            }
            if (imcontinue == null) break;
        }
    }
