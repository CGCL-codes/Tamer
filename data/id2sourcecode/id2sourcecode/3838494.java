    public RecordProgressDecorator(Learner learner, OutputStream outStream, int threadNumber, Configuration conf, boolean writeInZipFormat) {
        super(learner);
        config = conf;
        writeZip = writeInZipFormat;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setValidating(false);
            doc = factory.newDocumentBuilder().newDocument();
            if (writeZip) {
                outputStream = new ZipOutputStream(new java.io.BufferedOutputStream(outStream));
            } else {
                outputStream = outStream;
                topElement = doc.createElement(StatechumXML.ELEM_STATECHUM_TESTTRACE.name());
                doc.appendChild(topElement);
                topElement.appendChild(AbstractPersistence.endl(doc));
            }
            Configuration seriesConfiguration = config.copy();
            seriesConfiguration.setGdMaxNumberOfStatesInCrossProduct(0);
            series = new GraphSeries(doc, threadNumber, seriesConfiguration);
            initIO(doc, config);
        } catch (ParserConfigurationException e) {
            statechum.Helper.throwUnchecked("failed to construct DOM document", e);
        }
    }
