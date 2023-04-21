            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                File dtdFile = new File(fRule.getParentFile(), "ack.dtd");
                if (dtdFile.exists()) {
                    return new InputSource(new FileInputStream(dtdFile));
                }
                URL url = Thread.currentThread().getContextClassLoader().getResource("ack.dtd");
                if (url != null) {
                    return new InputSource(url.openStream());
                }
                return null;
            }
