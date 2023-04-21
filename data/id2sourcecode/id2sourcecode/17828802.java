    private Document parseXML(URL url) {
        try {
            Document ret = (Document) m_parsedFiles.get(url);
            if (ret != null) {
                return ret;
            }
            if (m_parser == null) {
                javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                m_parser = dbf.newDocumentBuilder();
                m_parser.setEntityResolver(new org.xml.sax.EntityResolver() {

                    public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
                        if (CONFIG_PUBLIC.equals(publicId)) {
                            URL dtdurl = parseURL("${openorb.home}config/OpenORB.dtd");
                            org.xml.sax.InputSource is = new org.xml.sax.InputSource(dtdurl.toString());
                            try {
                                is.setByteStream(dtdurl.openStream());
                            } catch (java.io.IOException ex) {
                            }
                            return is;
                        }
                        return null;
                    }
                });
            }
            ret = m_parser.parse(url.toString());
            m_parsedFiles.put(url, ret);
            return ret;
        } catch (final javax.xml.parsers.ParserConfigurationException ex) {
            final String msg = "ParserConfigurationException while parsing XML File \"" + url + "\"";
            throw new CascadingRuntimeException(msg + " (" + ex + ")", ex);
        } catch (final org.xml.sax.SAXException ex) {
            final String msg = "SAXException while parsing XML File \"" + url + "\"";
            throw new CascadingRuntimeException(msg + " (" + ex + ")", ex);
        } catch (final java.io.IOException ex) {
            final String msg = "IOException while parsing XML File \"" + url + "\"";
            throw new CascadingRuntimeException(msg + " (" + ex + ")", ex);
        }
    }
