    private Document parse(final String uri) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            con.addRequestProperty("Accept", "application/rdf+xml");
            Templates template = factory.newTemplates(new StreamSource(new FileInputStream("skos.xsl")));
            Transformer xformer = template.newTransformer();
            Source source = new StreamSource(url.openStream());
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Result result = new DOMResult(doc);
            xformer.transform(source, result);
            return doc;
        } catch (final ParserConfigurationException e) {
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final TransformerConfigurationException e) {
        } catch (final TransformerException e) {
        } catch (final MalformedURLException mex) {
        } catch (final IOException ioe) {
        }
        return null;
    }
