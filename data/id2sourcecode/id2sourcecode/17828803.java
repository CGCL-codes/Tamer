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
