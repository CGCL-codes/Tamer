                                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                                    URL url = URLFactory.createURL((locator[0] != null && locator[0].getSystemId() != null) ? locator[0].getSystemId() : null, systemId);
                                    InputSource i = new InputSource(url.openStream());
                                    i.setSystemId(url.toString());
                                    return i;
                                }
