    public void write(ProcessThread processThread) {
        build(processThread);
        try {
            System.out.println(document.toString());
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(outputFile);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer domTransformer = factory.newTransformer();
            domTransformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, null);
        } catch (TransformerFactoryConfigurationError e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, null);
        } catch (TransformerException e) {
            ExceptionManagerFactory.getExceptionManager().manageException(e, null);
        }
    }
