    public static CopyOnWriteArraySet<String> read(URL url, String propertyName) {
        if ((propertyName == null) || (propertyName.equals(""))) {
            return null;
        }
        CopyOnWriteArraySet<String> peers = new CopyOnWriteArraySet<String>();
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlif.setProperty("javax.xml.stream.isCoalescing", java.lang.Boolean.TRUE);
        xmlif.setProperty("javax.xml.stream.isNamespaceAware", java.lang.Boolean.TRUE);
        XMLStreamReader xmlr = null;
        BufferedInputStream stream = null;
        long starttime = System.currentTimeMillis();
        logger.info("Starting to parse the remote config xml[" + url + "]");
        int elementCount = 0;
        int topPropertyCounter = 0;
        int propertyTagLevel = 0;
        try {
            stream = new BufferedInputStream(url.openStream());
            xmlr = xmlif.createXMLStreamReader(stream, "utf8");
            int eventType = xmlr.getEventType();
            String curElement = "";
            String targetTagName = "property";
            String peerListAttrName = propertyName;
            boolean sentinel = false;
            boolean valueline = false;
            while (xmlr.hasNext()) {
                eventType = xmlr.next();
                switch(eventType) {
                    case XMLEvent.START_ELEMENT:
                        curElement = xmlr.getLocalName();
                        if (curElement.equals("property")) {
                            topPropertyCounter++;
                            propertyTagLevel++;
                            int count = xmlr.getAttributeCount();
                            if (count > 0) {
                                for (int i = 0; i < count; i++) {
                                    if (xmlr.getAttributeValue(i).equals(peerListAttrName)) {
                                        sentinel = true;
                                    }
                                }
                            }
                        }
                        if (sentinel && curElement.equals("value")) {
                            valueline = true;
                            String ipAd = xmlr.getElementText();
                            peers.add(ipAd);
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        break;
                    case XMLEvent.ATTRIBUTE:
                        if (curElement.equals(targetTagName)) {
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        if (xmlr.getLocalName().equals("property")) {
                            if (sentinel) {
                                sentinel = false;
                                valueline = false;
                            }
                            elementCount++;
                            propertyTagLevel--;
                        } else {
                        }
                        break;
                    case XMLEvent.END_DOCUMENT:
                }
            }
        } catch (MalformedURLException ue) {
            logger.log(Level.WARNING, "specified url was not correct", ue);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "remote config file cannot be read due IO problems", ex);
        } catch (XMLStreamException ex) {
            logger.log(Level.WARNING, "remote config file (" + url + ") cannot be read due to unexpected processing conditions", ex);
        } finally {
            if (xmlr != null) {
                logger.info("closing the xml stream reader");
                try {
                    xmlr.close();
                } catch (XMLStreamException ex) {
                }
            }
            if (stream != null) {
                logger.info("closing the stream");
                try {
                    stream.close();
                } catch (IOException ex) {
                }
            }
        }
        return peers;
    }
