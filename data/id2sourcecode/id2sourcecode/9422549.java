    private Document parseCurrentURL() throws ProcessorException {
        URL url = this.docURL.peek();
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream stream = url.openStream();
            InputSource inputSource = new InputSource(stream);
            inputSource.setSystemId(url.toString());
            document = builder.parse(inputSource);
            stream.close();
        } catch (IOException e) {
            throw new ProcessorException("I/O error while parsing \"" + url + "\".", e);
        } catch (ParserConfigurationException e) {
            throw new ProcessorException("Parser configuration error while parsing \"" + url + "\".", e);
        } catch (SAXParseException e) {
            throw new ProcessorException("SAX parsing error while parsing \"" + url + "\".", e);
        } catch (SAXException e) {
            throw new ProcessorException("SAX exception while parsing \"" + url + "\".", e);
        }
        return document;
    }
