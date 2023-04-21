                                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                                        URL url = URLFactory.createURL(schemaSystemId, systemId);
                                        InputSource i = new InputSource(url.openStream());
                                        i.setSystemId(url.toString());
                                        return i;
                                    }
