    private static DocumentBuilder getDocumentBuilder(final String dtd_symbol, final URL dtd_url) {
        DocumentBuilderFactory doc_builder_factory = DocumentBuilderFactory.newInstance();
        doc_builder_factory.setValidating(true);
        DocumentBuilder doc_builder = null;
        try {
            doc_builder = doc_builder_factory.newDocumentBuilder();
        } catch (Exception e) {
            XRepository.getLogger().error(e, "An error occured while trying to create new XML document builder!");
            XRepository.getLogger().error(e, e);
            return null;
        }
        doc_builder.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String public_id, String system_id) {
                if (dtd_symbol != null && system_id.endsWith(dtd_symbol)) {
                    try {
                        Reader reader = new InputStreamReader(dtd_url.openStream());
                        return new InputSource(reader);
                    } catch (Exception e) {
                        XRepository.getLogger().error(this, "An error occured while trying to resolve the main DTD!");
                        XRepository.getLogger().error(this, e);
                        return null;
                    }
                } else return null;
            }
        });
        return doc_builder;
    }
