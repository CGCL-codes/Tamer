    private void runTest(IDerivator testDerivator, String derivatorName, String testData) throws MM4UCannotOpenMediaElementsConnectionException, IOException, MM4UGeneratorException {
        RDFURIMediaElementsConnector connector = new RDFURIMediaElementsConnector();
        connector.openConnection();
        MM4UDeserializer deSerial = new MM4UDeserializer(connector);
        URL url = new URL(testData);
        InputStream inStream = url.openStream();
        deSerial.doDeSerialize(inStream, Constants.getValue("derivation_url"), false);
        IVariable var = testDerivator.doDerivate(deSerial);
        inStream.close();
        IGenerator myGenerator = GeneratorToolkit.getFactory(OUTPUT_FORMAT);
        IMultimediaPresentation presentation = myGenerator.doTransform(var, "DerivatorTest", new SimpleUserProfile());
        presentation.store(OUTPUT_PATH + derivatorName);
    }
