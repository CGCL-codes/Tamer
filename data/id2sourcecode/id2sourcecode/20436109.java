        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            if ("Topic".equals(qName)) {
                curSection = atts.getValue("r:id");
            } else if ("ExternalPage".equals(qName)) {
                if ((!includeAdult) && curSection.startsWith("Top/Adult")) {
                    return;
                }
                if (topicPattern != null && !topicPattern.matcher(curSection).matches()) {
                    return;
                }
                String url = atts.getValue("about");
                int hashValue = MD5Hash.digest(url).hashCode();
                hashValue = Math.abs(hashValue ^ hashSkew);
                if ((hashValue % subsetDenom) != 0) {
                    return;
                }
                curURL = url;
            } else if (curURL != null && "d:Title".equals(qName)) {
                titlePending = true;
            } else if (curURL != null && "d:Description".equals(qName)) {
                descPending = true;
            }
        }
