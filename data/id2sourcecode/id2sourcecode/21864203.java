    private NodeImpl loadVarFromURI(String uri) throws XPathException {
        try {
            URL url = new URL(uri);
            InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            InputSource src = new InputSource(isr);
            SAXParser parser = factory.newSAXParser();
            XMLReader xr = parser.getXMLReader();
            SAXAdapter adapter = new SAXAdapter(context);
            xr.setContentHandler(adapter);
            xr.setProperty(Namespaces.SAX_LEXICAL_HANDLER, adapter);
            xr.parse(src);
            isr.close();
            return (NodeImpl) adapter.getDocument();
        } catch (MalformedURLException e) {
            throw new XPathException(this, e);
        } catch (IOException e) {
            throw new XPathException(this, e);
        } catch (SAXException e) {
            throw new XPathException(this, e);
        } catch (ParserConfigurationException e) {
            throw new XPathException(this, e);
        }
    }
