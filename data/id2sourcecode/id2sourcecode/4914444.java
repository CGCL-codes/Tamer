    private void getTestPlanFromRecorder() {
        TransformerFactory m_transformerFactory = TransformerFactory.newInstance();
        m_transformerFactory.setErrorListener(new ErrorListener() {

            public void warning(TransformerException e) throws TransformerException {
                logger.warn("Warning", e);
            }

            public void error(TransformerException e) throws TransformerException {
                logger.error("Error", e);
            }

            public void fatalError(TransformerException e) throws TransformerException {
                logger.error("Fatal", e);
            }
        });
        FileOutputStream resultStream = null;
        try {
            resultStream = new FileOutputStream(new File(testPlanFile));
            Transformer transformer = m_transformerFactory.newTransformer(new StreamSource(new FileInputStream("resource/grinder/recorder.xsl")));
            transformer.transform(new StreamSource(new ByteArrayInputStream(boStream.toByteArray())), new StreamResult(resultStream));
        } catch (Exception e) {
            logger.error("Failed to transform recording to test plan", e);
        } finally {
            if (boStream != null) try {
                boStream.close();
            } catch (IOException e1) {
            }
            if (resultStream != null) try {
                resultStream.close();
            } catch (IOException e1) {
            }
        }
    }
