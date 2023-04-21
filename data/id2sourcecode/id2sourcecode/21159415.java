    private Element convertModelToDomElement(final Model model) throws InternalServiceFault, ParserConfigurationException, TransformerConfigurationException, TransformerException {
        Document doc = null;
        try {
            PipedInputStream pIn = new PipedInputStream();
            final PipedOutputStream pOut = new PipedOutputStream();
            pOut.connect(pIn);
            Thread writerThread = new Thread(new Runnable() {

                public void run() {
                    model.write(pOut, "RDF/XML-ABBREV");
                    try {
                        pOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            writerThread.start();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = factory.newDocumentBuilder();
            doc = domBuilder.parse(pIn);
            pIn.close();
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw new InternalServiceFault("The constructed RDF/XML " + "graph doesn't appear to be well-formed.", e.getMessage(), e);
        } catch (IOException e) {
            throw new InternalServiceFault("Unexpected error.", e.getMessage(), e);
        }
        return doc.getDocumentElement();
    }
