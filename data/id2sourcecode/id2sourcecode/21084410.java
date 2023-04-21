    public static List loadDefaultsFromURL(MTP mtp, URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
            IXMLReader reader = new StdXMLReader(in);
            parser.setReader(reader);
            XMLElement el = (XMLElement) parser.parse();
            if (isName(el, METATYPE_NS, VALUES)) {
                List propList = loadValues(mtp, el);
                setDefaultValues(mtp, propList);
                return propList;
            } else {
                for (Enumeration e = el.enumerateChildren(); e.hasMoreElements(); ) {
                    XMLElement childEl = (XMLElement) e.nextElement();
                    if (isName(childEl, METATYPE_NS, VALUES)) {
                        List propList = loadValues(mtp, childEl);
                        setDefaultValues(mtp, propList);
                        return propList;
                    }
                }
            }
            throw new XMLException("No values tag in " + url, el);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to load " + url + " " + e);
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {
            }
        }
    }
