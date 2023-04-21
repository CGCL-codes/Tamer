    @Override
    public void consume() {
        if (!hookedUp && faucetTemplate == null) throw new XformationException("Sink has not been set up correctly: " + "faucet has not been set");
        switch(type) {
            case File:
                try {
                    outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
                } catch (FileNotFoundException e) {
                    throw new XformationException("Unable to create output stream", e);
                }
                break;
            case Stream:
                break;
        }
        if (!hookedUp) {
            if (faucetTemplate instanceof IPipelineItem) {
                ((IPipelineItem) faucetTemplate).consume(this);
            }
        }
        try {
            int counter = 0;
            while (true) {
                DequePayload payload = null;
                try {
                    payload = deque.takeFirst();
                } catch (InterruptedException ie) {
                }
                if (payload == null) break;
                IFaucet faucet = payload.faucet;
                if (logger.isTraceEnabled()) logger.trace("Retrieved faucet: " + faucet + " from the mux deque");
                if (faucet == null) break;
                ZipEntry entry = new ZipEntry("entry" + counter++);
                outputStream.putNextEntry(entry);
                StreamResult result = new StreamResult(outputStream);
                InputSource inputSource = null;
                if (faucet instanceof IPipelineItem) {
                    inputSource = (InputSource) ((IPipelineItem) faucet).consume(this);
                }
                try {
                    Object faucetObject = faucet.getSource(ContentType.XML);
                    if (logger.isTraceEnabled()) logger.trace("Sink is using reader: " + faucetObject);
                    SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.newInstance();
                    Transformer transformer = stf.newTransformer();
                    SAXSource transformSource = new SAXSource((XMLReader) faucetObject, inputSource);
                    transformer.transform(transformSource, result);
                } catch (Exception e) {
                    throw new XformationException("Unable to set up transform", e);
                }
                faucet.dispose();
            }
            outputStream.close();
            faucetTemplate.dispose();
        } catch (IOException ioe) {
            logger.error("Error while consuming input", ioe);
            throw new XformationException("Unable to transform stream", ioe);
        }
    }
