        HTTPSearchResult(String query) {
            String url = (String) parameters.get("url");
            if (url.lastIndexOf("?") >= 0) url += "&query=" + URLEncoder.encode(query); else url += "?query=" + URLEncoder.encode(query);
            XMLReader parser;
            try {
                parser = new DefaultXMLReaderFactory().createXMLReader();
            } catch (SAXException e) {
                throw new OntopiaRuntimeException("Problems occurred when creating SAX2 XMLReader", e);
            }
            SearchHandler handler = new SearchHandler();
            parser.setContentHandler(handler);
            try {
                URLConnection conn = new URL(url).openConnection();
                parser.parse(new InputSource(conn.getInputStream()));
            } catch (SAXParseException e) {
                throw new OntopiaRuntimeException("XML parsing problem: " + e.toString() + " at: " + e.getSystemId() + ":" + e.getLineNumber() + ":" + e.getColumnNumber(), e);
            } catch (SAXException e) {
                if (e.getException() instanceof IOException) throw new OntopiaRuntimeException(e.getException());
                throw new OntopiaRuntimeException(e);
            } catch (Exception e) {
                throw new OntopiaRuntimeException(e);
            }
            List _hits = handler.getHits();
            this.hits = _hits.iterator();
        }
