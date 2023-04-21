        public Source resolve(String href, String base) throws TransformerException {
            try {
                if ("".equals(base)) base = null;
                URL url = URLFactory.createURL(base, href);
                return new SAXSource(XMLUtils.newSAXParser(false).getXMLReader(), new InputSource(url.openStream()));
            } catch (SAXException e) {
                throw new TransformerException(e);
            } catch (IOException e) {
                throw new TransformerException(e);
            }
        }
